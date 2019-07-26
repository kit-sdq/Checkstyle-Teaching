import os
import subprocess


def print_java_info(document):
    """Print information about the java executable"""
    if not os.environ.has_key("JAVA"):
        print_debug_info(document, "Java", "JAVA environment variable not set")
        return

    java = os.environ["JAVA"]

    executable = which(java)
    if not executable:
        print_debug_info("Java", "Java executable not found in the path")
        return

    process = subprocess.Popen([executable, '-version'], shell=False, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    stdout,stderr = process.communicate()

    print_debug_info(document, "Java", stdout.split("\n") + stderr.split("\n"))


def print_java_secure_info(document):
    """Print information about the secure java executable"""
    if not os.environ.has_key("JAVA_SECURE"):
        print_debug_info(document, "JAVA_SECURE not in environment")
        return

    java = os.environ["JAVA_SECURE"]

    executable = which(java)
    if not executable:
        print_debug_info(document, "Java secure", "Java executbale not found")
        return

    process = subprocess.Popen([executable, '-version'], shell=False, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    stdout,stderr = process.communicate()

    print_debug_info(document, "Java secure", stdout.split("\n") + stderr.split("\n"))


def print_debug_info(document, title, message):
    with document.div({'class': 'outer'}):
        with document.div({'class': 'inner'}):
            with document.h5():
                document.cdata(title + ':')
            with document.div({'class': 'section'}):
                for line in message:
                    with document.div():
                        document.cdata(line)


def which(program):
    """Identify the full path to the executable"""
    def is_executable(fpath):
        return os.path.isfile(fpath) and os.access(fpath, os.X_OK)

    fpath, fname = os.path.split(program)
    if fpath:
        if is_executable(program):
            return program
    else:
        for path in os.environ["PATH"].split(os.pathsep):
            path = path.strip('"')
            exe_file = os.path.join(path, program)
            if is_executable(exe_file):
                return exe_file

    return None
