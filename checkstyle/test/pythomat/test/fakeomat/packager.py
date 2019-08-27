import os
import string
import zipfile

from pythomat import contexts


def without_dots(path):
    """Strip leading ../ from a path."""
    relpath = path
    while relpath.startswith("../"):
        relpath = relpath[3:]
    return relpath


def modules_and_ressources(argument):
    """Split a list into modules and ressources.

    The function returns two lists, one for the modules and one for the
    ressources.  Everything ending in '.py' in the list is a module, everything
    else is a ressource.  Ressources are added recursively, modules aren't."""
    arguments = argument.split(",") if len(argument) else []
    modules = [a for a in arguments if a.endswith(".py")]
    non_modules = [a for a in arguments if not a.endswith(".py")]

    ressources = []
    for filename in non_modules:
        if os.path.isdir(filename):
            for d, ss, fs in os.walk(filename):
                for entry in (os.path.join(d, f) for f in fs):
                    ressources.append(entry)
        else:
            ressources.append(filename)

    return modules, ressources


def package(target, root, subdirs, tests=[], modules=[], ressources=[], main=None):
    """Package py files from a directory plus additional files"""
    archive = zipfile.PyZipFile(target, 'w')
    for directory, dirs, files in os.walk(root):
        relpath = os.path.relpath(directory, root)
        if relpath in subdirs:
            for filename in files:
                if filename.endswith(".py"):
                    fullname = os.path.abspath(os.path.join(directory, filename))
                    with contexts.CaptureStdoutGuard() as guard:
                        archive.writepy(fullname, relpath)
                    if len(guard.lines) != 0:
                        raise SyntaxError("Could not compile " + filename + '\n' + guard.lines)

    for test in tests:
        archive.write(test, os.path.join("tests", without_dots(test)))

    for module in modules:
        with contexts.CaptureStdoutGuard() as guard:
            archive.writepy(module)

        if len(guard.lines) != 0:
            raise SyntaxError("Could not compile " + module + '\n' + guard.lines)

    for ressource in ressources:
        archive.write(ressource, os.path.join("ressources", without_dots(ressource)), zipfile.ZIP_STORED)

    if main:
        archive.writestr("__main__.py", main)

    archive.close()


def create_main(output, checkers):
    checkers = ",\n".join(['"' + checker + '"' for checker in checkers])
    return string.Template("""import sys
import fakeomat

fakeomat.run(
    checkers = [
$checkers
    ],
    output = "$output"
)""").substitute(output=output, checkers=checkers)


def parsed(string):
    """Parse a string into a command and its argument separated by a :"""
    try:
        command, argument = [s.strip() for s in string.split(":", 1)]
    except ValueError:
        command, argument = string, ""
    return command, argument


# TODO unused argument submission
def run(checkers, submission, output):
    adapted_checkers = []
    tests = []
    modules = []
    # TODO typo
    ressources = []

    # adapt the checkers as needed
    for command, argument in [parsed(checker) for checker in checkers]:
        if command == "pythomat":
            # copy ourselves to the submission directory
            mods, ress = modules_and_ressources(argument)
            modules += mods
            ressources += ress
            command = "copy"
            argument = '" + sys.argv[0] + "'
        elif command == "copy":
            ressources.append(argument)
            command = "extract"
            argument = '" + sys.argv[0] + ":' + 'ressources/' + without_dots(argument)
        elif command == "script":
            tests.append(argument)
            argument = '" + sys.argv[0] + ":' + 'tests/' + without_dots(argument)

        adapted_checkers.append(command + ":" + argument)

    # package pythomat
    main = create_main(output, adapted_checkers)
    root = os.path.abspath(os.path.split(os.path.dirname(__file__))[0])
    package("pythomat.zip", root, ["pythomat", "fakeomat", "chardet"], tests=tests, modules=modules,
            ressources=ressources, main=main)

    print("Run 'python pythomat.zip <submission>'")
