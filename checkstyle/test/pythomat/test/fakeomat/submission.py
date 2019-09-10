import os
import re
import zipfile

from .checkers import Checker
from .checkers import CheckerFailure
from .checkers import SubmissionFailure


class FindJava(Checker):
    """Find java and store the paths in the environment.

    Sets the environment variable JAVA and JAVA_SECURE, similar to how
    Praktomat does it."""

    def title(self):
        return "Find java executable"

    def run(self):
        # TODO: we should actally try to find java and create a properly
        #       secured JAVA_SECURE instead of this.
        os.putenv("JAVA", "java")
        os.putenv("JAVA_SECURE", "java")


def write_unicode(filename, contents):
    """Write a file trying to encode contents as unicode."""
    try:
        # chardet is not part of the python default packages
        # we want to give a nice error message, so we try catch this.
        import chardet
    except ImportError as e:
        raise CheckerFailure("{0} (hint: try to install python-chardet)".format(e))

    import codecs
    # TODO str/bytes
    as_unicode = str(contents, chardet.detect(contents)['encoding'])
    dirname, basename = os.path.split(filename)
    if not os.path.exists(dirname):
        os.makedirs(dirname)

    with codecs.open(os.path.join(filename), 'w', encoding='utf-8') as out_handle:
        out_handle.write(as_unicode)


def write_bytes(filename, contents):
    """Write a file without caring about encodings or similar"""
    dirname, basename = os.path.split(filename)
    if not os.path.exists(dirname):
        os.makedirs(dirname)

    with open(os.path.join(filename), 'w') as out_handle:
        out_handle.write(contents.decode())


def write_file(filename, contents):
    """Write a file, if it's java care for unicode, otherwise not"""
    basename, ext = os.path.splitext(filename)
    if ext.lower() == ".java":
        write_unicode(filename, contents)
    else:
        write_bytes(filename, contents)


def extract_submission_files(submission, dst_dir):
    """Unzip all files in the zip file to the temporary directory"""
    # TODO use with instead of try-finally?
    try:
        submission_zip = zipfile.ZipFile(submission, 'r')
        for filename in submission_zip.namelist():
            if not filename.endswith('/'):
                handle = submission_zip.open(filename)
                contents = handle.read()
                write_file(os.path.join(dst_dir, filename), contents)
    finally:
        submission_zip.close()


def copy_submission_directory(src_dir, dst_dir):
    """Copy all files from a directory to the temporary directory"""
    for d, s, f in os.walk(src_dir):
        for filename in [os.path.join(d, filename) for filename in f if not filename.endswith(".class")]:
            with open(filename, 'rb') as handle:
                contents = handle.read()
            relpath = os.path.relpath(filename, src_dir)
            write_file(os.path.join(dst_dir, relpath), contents)


def copy_submission_file(filename, dst_dir):
    """Copy the submission file to the submission directory"""
    with open(filename, 'r') as handle:
        contents = handle.read()
    dirname, basename = os.path.split(filename)
    write_file(os.path.join(dst_dir, basename), contents)


def list_submission_files(directory):
    """List all files in the temporary directory"""
    files = []
    for d, s, f in os.walk(directory):
        for filename in f:
            path = os.path.normpath(d + '/' + filename)
            path = path[len(directory) + 1:]
            files.append(path)

    return files


class CopySubmissionChecker(Checker):
    def title(self):
        return "Copying submission"

    def run(self):
        """Prepare the temporary directory by copying the submission to it"""
        submission = self.arguments

        if isinstance(submission, str):
            submission = [submission]

        if len(submission) == 0:
            raise SubmissionFailure("No submission files specified")

        # Copy all files to the temporary directory
        for filename in submission:
            if not os.path.exists(filename):
                raise CheckerFailure("File does not exist: " + filename)

            if zipfile.is_zipfile(filename):
                extract_submission_files(filename, self.tempdir)
            elif os.path.isdir(filename):
                copy_submission_directory(filename, self.tempdir)
            else:
                copy_submission_file(filename, self.tempdir)

        # List all files in the temporary directory
        submission_files = list_submission_files(self.tempdir)
        for filename in submission_files:
            with self.document.div():
                self.document.cdata(filename)


package_re = re.compile("\s*package\s+([^\s]+)\s*;\s*")


def fully_qualified_classname(filename):
    """
    Naively try to find a package line in the first few lines of the file.
    Note that this does not handle comments correctly.
    """

    classname = os.path.splitext(os.path.basename(filename))[0]

    from itertools import islice
    with open(filename, 'r') as java_file:
        for line in islice(java_file, 0, 10):
            match = package_re.match(line)
            if match:
                # file contains a package line, use it
                return match.group(1) + '.' + classname

    # file seems to be in the default package
    return classname


main_re = re.compile(".*\s+public\s+static\s+void\s+main\s*[(].*")


def contains_main_method(filename):
    """Search for the main method in the file."""
    with open(filename, 'r') as javafile:
        for line in javafile:
            if main_re.match(line):
                return True


def search_main_method_in_directory(root_directory):
    """Search for a class containing a main method."""
    # open all java files in the directory
    # search them for a file containing a main method
    # return the fully qualified name of that class
    found = None
    for (directory, subdirectories, filenames) in os.walk(root_directory):
        for filename in filenames:
            (base, extension) = os.path.splitext(filename)
            if extension.lower() == ".java":
                full_filename = directory + '/' + filename
                if contains_main_method(full_filename):
                    if found is None:
                        found = full_filename
                    else:
                        raise SubmissionFailure(
                            "No unique main method found (" + filename + " and " + os.path.split(found)[1] + ")")

    return fully_qualified_classname(found) if found else None


class SearchMainMethodChecker(Checker):
    def title(self):
        return "Searching for main method"

    def run(self):
        """Search the main method in the list of java files"""

        main_method = search_main_method_in_directory(self.tempdir)
        if main_method:
            os.putenv("PROGRAM", main_method)
            with self.document.div():
                self.document.cdata("Main method found in: " + main_method)

        if not main_method:
            raise SubmissionFailure("No main method found")
