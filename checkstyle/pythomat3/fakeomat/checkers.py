import re
import os
import shutil
import subprocess
import sys
import zipfile

from . import packager
from pythomat.which import which
from pythomat.contexts import InternalTimeoutGuard
from pythomat.contexts import ExternalTimeoutException


class CheckerFailure(Exception):
    """This is when it is the python programmer's or the trainer's fault.

    This usually results in a stack trace.
    """
    pass


class SubmissionFailure(Exception):
    """This is when it is the student's fault.

    No stack trace should be printed in this case. We already know who's to
    blame.
    """
    pass


class Checker(object):
    """Base class for all checker classes.

    Derive from this class and implement a run method to create new
    checkers.  Optionally implement title() to return a description
    for display when the checker is run.
    """

    # each checker should also do the packaging for itself:
    # provide a 'package' method to place everything in pythomat
    # provide a 'run_packaged' method to use the pythomat packaged data
    def __init__(self, arguments):
        self.arguments = arguments

    def title(self):
        return self.__class__.__name__

    def print_title(self):
        """Print the Checker's title to the document."""
        with self.document.h3(
                {'class': "togglebutton", 'onClick': "javascript:slide(this.firstChild, this.nextSibling);"}):
            with self.document.span({'class': 'togglesymbol'}):
                self.document.cdata("+")
            self.document.cdata(self.title())

    def decorated_run(self, document, tempdir):
        """Run a checker's run(), decorated with html title and result."""
        self.document = document
        self.tempdir = tempdir

        try:
            self.print_title()
            with self.document.div({'class': 'hideable', 'style': 'display:none'}):
                self.run()

        except SubmissionFailure as e:
            with self.document.div({'class': 'failed'}):
                self.document.cdata("Failed: " + str(e))
            raise

        except Exception as e:
            import traceback
            with self.document.div({'class': 'failed'}):
                self.document.cdata("Crashed: " + str(e))
                with self.document.div({'style': "white-space:pre"}):
                    self.document.cdata(traceback.format_exc())
            raise

        else:
            with self.document.div({'class': 'passed'}):
                self.document.cdata("Success")


class SearchMainDummyChecker(Checker):
    """This is a dummy checker to signalize that the main method
     should be searched at a manual point in the test pipeline"""
    pass


class FileCopyChecker(Checker):
    """Copy files into the temporary directory."""

    def title(self):
        return "Copying file"

    def run(self):
        '''Copy the file over to the temporary directory'''
        with self.document.div():
            self.document.cdata(self.arguments)

        # make sure the source file exists
        if not os.path.exists(self.arguments):
            raise CheckerFailure("File " + self.arguments + " does not exists")

        normpath = os.path.normpath(self.arguments)

        # make sure the directory exists (what about absolute paths?)
        if os.path.isabs(normpath):
            raise CheckerFailure("Absolute paths currently not supported")

        # relative paths are kept, unless they are to an outside directory
        directory = os.path.dirname(normpath)
        if directory == "..":
            target_directory = self.tempdir
        else:
            target_directory = os.path.join(self.tempdir, directory)

        if len(directory) != 0:
            if not os.path.exists(target_directory):
                os.makedirs(target_directory)
            elif not os.path.isdir(target_directory):
                raise CheckerFailure("Target directory exists, but is not a directory: " + target_directory)

        # and copy over the file
        shutil.copy(self.arguments, target_directory)


class FileRemovalChecker(Checker):
    """Copy files into the temporary directory."""

    def title(self):
        return "Removing file"

    def run(self):
        '''Remove a file from the temporary directory'''
        with self.document.div():
            self.document.cdata(self.arguments)

        fullPath = os.path.join(self.tempdir, self.arguments)
        if os.path.exists(fullPath):
            os.remove(fullPath)


class ExtractFileChecker(Checker):
    """Extract files from a zip archive into the temporary directory."""

    def title(self):
        return "Extracting archive"

    def run(self):
        """Extract a file from a zip archive"""
        with self.document.div():
            self.document.cdata(self.arguments)

        # extract the file, remove 'ressources' prefix
        archivename, filename = self.arguments.split(':', 1)
        archive = zipfile.ZipFile(archivename, 'r')
        info = archive.getinfo(filename)
        info.filename = os.path.relpath(filename, "ressources")
        archive.extract(info, self.tempdir)


class PythomatChecker(Checker):
    """Package a pythomat file."""

    def title(self):
        return "Packaging pythomat"

    def run(self):
        modules, ressources = packager.modules_and_ressources(self.arguments)

        if len(modules) != 0:
            with self.document.div():
                self.document.cdata("Injecting modules:")
            for module in modules:
                with self.document.div():
                    self.document.cdata(module)

        if len(ressources) != 0:
            with self.document.div():
                self.document.cdata("Injecting ressources:")
            for ressource in ressources:
                with self.document.div():
                    self.document.cdata(ressource)

        # place pythomat in the current directory
        directory = os.path.abspath(os.path.split(os.path.dirname(__file__))[0])

        # data is simply zipped, code is compiled and zipped
        packager.package("pythomat.zip", directory, ["pythomat"], modules=modules, ressources=ressources)
        shutil.copy("pythomat.zip", self.tempdir)


class CheckstyleChecker(Checker):
    """Placeholder for checkstyle checkers.

    This checker is currently non-functional. It is intended as a placeholder
    for a checkstyle checker in the real Praktomat system.  Essentially this is
    just a reminder to upload the checkstyle checks into Praktomat.  Now that
    we have script checker for checkstyle, we should get rid of this
    checker.
    """

    def title(self):
        return "Checking style"

    def run(self):
        '''Run checkstyle on the java files in the directory'''
        with self.document.div():
            self.document.cdata("Skipped (currently not supported)")


class ZipDirectoryChecker(Checker):
    """Zip a directory and copy the zip file to the temporary directory.

    This is a historic checker and was originally used to copy archives
    containing test data to the temporary directory.  It should probably not be
    used anymore, now that pythomat supports embedding ressources in the
    pythomat archive itself.
    """

    def title(self):
        return "Copying zip file"

    def run(self):
        '''Zip up the directory 'arguments' to a zip file in tempdir'''
        basename = os.path.basename(self.arguments)
        zip_file = zipfile.ZipFile(self.tempdir + '/' + basename + ".zip", 'w')
        for content in [x for x in os.listdir(self.arguments) if not os.path.splitext(x)[1] == ".swp"]:
            zip_file.write(self.arguments + '/' + content, content)
        zip_file.close()


class CompilerChecker(Checker):
    """Run the java compiler on the java files in the temporary directory."""

    def title(self):
        return "Compiling"

    def run(self):
        '''run the java compiler on all java files in the directory'''
        # find all java files
        java_files = []
        for directory, s, filenames in os.walk(self.tempdir):
            for filename in [x for x in filenames if os.path.splitext(x)[1] == ".java"]:
                java_files.append(directory + '/' + filename)

        if not which("javac"):
            raise CheckerFailure("Java compiler not found in PATH")

        command_line = ["javac", "-d", self.tempdir]

        jar_files = []
        for directory, s, filenames in os.walk(self.tempdir):
            for filename in [x for x in filenames if os.path.splitext(x)[1] == ".jar"]:
                jar_files.append(directory + '/' + filename)

        if jar_files or self.arguments:
            command_line += ["-classpath"]
        if self.arguments:
            command_line += [self.arguments]
        if jar_files:
            command_line += [";".join(["."] + jar_files)]

        with self.document.div():
            self.document.cdata("Compiling:")
        for java_file in (os.path.relpath(name, self.tempdir) for name in java_files):
            with self.document.div():
                self.document.cdata(java_file)

        command_line += java_files

        # compile the sources
        compiler = subprocess.Popen(command_line, cwd=self.tempdir, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        stdout, stderr = compiler.communicate()

        # print compiler output
        if len(stdout) + len(stderr) != 0:
            with self.document.pre({'style': 'font-family:monospace;color:red;'}):
                self.document.cdata(stdout)
                self.document.cdata(stderr)

        # and evaluate the return code
        if compiler.returncode != 0:
            raise CheckerFailure("Compilation not successful.")


def list_files_relative(directory):
    files = []
    for d, s, f in os.walk(directory):
        for filename in f:
            path = os.path.normpath(d + '/' + filename)
            path = path[len(directory) + 1:]
            files.append(path)
    return files


def create_temp_script(tempdir, src_name):
    '''Copy the file src_name to dst_name and make it executable'''
    # TODO: we don't replace strings anymore, so a simple shutil.copy should suffice
    dst_name = tempdir + '/' + os.path.basename(src_name)
    with open(src_name, 'r') as src:
        with open(dst_name, 'w') as dst:
            for line in src:
                dst.write(line)

    # set mode to 0744
    import stat
    os.chmod(dst_name, stat.S_IRUSR | stat.S_IWUSR | stat.S_IXUSR | stat.S_IRGRP | stat.S_IROTH)
    return dst_name


def unzip_script(tempdir, scriptname):
    """Extract a script from the zip file"""
    zip_filename, script_filename = scriptname.split(':', 1)
    dst_name = os.path.join(tempdir, script_filename)

    zip_file = zipfile.ZipFile(zip_filename)
    zip_file.extract(script_filename, tempdir)

    import stat
    os.chmod(dst_name, stat.S_IRUSR | stat.S_IWUSR | stat.S_IXUSR | stat.S_IRGRP | stat.S_IROTH)
    return dst_name


class ScriptChecker(Checker):
    """Run a script checker.

    This passes a list of all files in the temporary directory as arguments to
    the script, just like the real praktomat does. A minor difference is, that
    this scripts copies the script file to the temporary directory and
    Praktomat does not.
    """

    def title(self):
        return "Running " + self.arguments

    def run(self):
        '''Run a single script checker'''
        error = None

        if not ":" in self.arguments:
            script = create_temp_script(self.tempdir, self.arguments)
        else:
            script = unzip_script(self.tempdir, self.arguments)

        # call python explicitly (windows needs this)
        name, ext = os.path.splitext(script)
        if ext == 'py':
            command = [script]
        else:
            command = ['python', script]

        all_files = list_files_relative(self.tempdir)

        process = subprocess.Popen(command + all_files, cwd=self.tempdir, stdout=subprocess.PIPE,
                                   stderr=subprocess.PIPE)
        try:
            with InternalTimeoutGuard(60, process):
                stdout, stderr = process.communicate()
        except ExternalTimeoutException as e:
            raise SubmissionFailure("Timeout occured")

        with self.document.div():
            self.document.raw(stdout.decode())
            self.document.raw(stderr.decode())

        if process.returncode != 0:
            raise SubmissionFailure("Return code should have been 0 but was " + str(process.returncode))


class UnidentifiedChecker(Checker):
    """This is pseudo checker for handling parsing failures."""

    def title(self):
        return "Unknown checker"

    def run(self):
        raise CheckerFailure("Unknown Checker")


def parse_checker_line(line):
    """Parse a single checker command line.

    Create a checker for this command.
    """
    try:
        command, arguments = [field.strip() for field in line.split(":", 1)]
    except ValueError:
        command, arguments = line, ""

    if command == "compile" or command == "compiler" or command == "javac":
        return CompilerChecker(arguments)
    elif command == "pythomat":
        return PythomatChecker(arguments)
    elif command == "checkstyle":
        return CheckstyleChecker(arguments)
    elif command == "script":
        return ScriptChecker(arguments)
    elif command == "copy":
        return FileCopyChecker(arguments)
    elif command == "remove":
        return FileRemovalChecker(arguments)
    elif command == "zip":
        return ZipDirectoryChecker(arguments)
    elif command == "extract":
        return ExtractFileChecker(arguments)
    elif command == "searchmain":
        return SearchMainDummyChecker("")
    else:
        return UnidentifiedChecker(arguments)


def parse_checker_lines(lines):
    """Parse a list of checker command lines.

    Create a list of checkers from it.
    """
    return [parse_checker_line(line) for line in lines]
