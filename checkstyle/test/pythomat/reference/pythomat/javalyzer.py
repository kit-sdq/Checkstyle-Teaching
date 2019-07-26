import os
import subprocess

from patterns import match
from checker import Checker
from exc import ConfigurationError
from which import which


def run(files, filenames=None, packages=None, imports=None, classes=None, enums=None, methods=None, description=None):
    """Check if the packages, class and methods conform to the rules.

    The list of rules is parsed first to last. The first rule that matches
    decides if a file is accepted or rejected.

    The available rules are:

    * accept <name pattern>
      An accept immediately accepts the name.

    * reject <name pattern> <message>
      A reject rule immediately rejectes the name.

    * require <name pattern> <message>
      A require rule does not immediately stop processing further rules for
      this name, but if, in the end, none of the names matched the
      rule, the set of names will be rejected.

    * warn <name pattern> <message>
      A warn rule prints a warning if the rule matches, but unlike a reject
      rule it does not stop processing and the name might be accepted by a
      later rule.

    * expect <name pattern> <message>
      An expect rule does not influence acceptance of the name or the set
      of names, but only prints a warning if the rule never matches.

    A <name pattern> is a unix filename matcher (e.g. using * for matching any sequence of characters).
    A <message> is a string template to be displayed to the user when the rule
    is violated and containing an explanation of why the name was rejected
    by the script.  In this template ${name} can be used as a placeholder
    for the input name and ${pattern} as a placeholder for the pattern.

    """


    checker = Checker(description=description)
    with checker.context():
        checker.initialize()

        file_list = [filename for filename in files if filename != "pythomat.zip"]
        package_list = []
        import_list = []
        class_list = []
        enum_list = []
        method_list = []

        java = checker.java_insecure
        if not java or not which(java):
            raise ConfigurationError("Could not find java executable")

        # identify all submitted java files
        java_files = [filename for filename in file_list if filename.lower().endswith(".java")]

        # check if the required jar files do exist
       # jars = ["checkstyle-teaching-1.0.jar"]#, "checkstyle-5.6-all.jar"]
       # for jar in jars:
       #     if not os.path.isfile(jar):
       #         raise ConfigurationError("Missing jar archive " + jar)

        # run checkstyle in lists mode on all java files
       # command_line = [java, "-classpath", ":".join(jars), "edu.kit.checkstyle.ListThings"] + java_files
       # process = subprocess.Popen(command_line, shell=False, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
       # stdout, stderr = process.communicate()
       # returncode = process.returncode

        #if returncode != 0:
        #    raise ConfigurationError("Return code of checkstyle was " + str(returncode) + "\n" + stdout + "\n" + stderr)

        # split the output
        '''
        for line in stdout.split("\n"):

            # skip empty lines
            if len(line) == 0:
                continue

            # split line at the colon
            splitted = line.split(": ", 1)
            if len(splitted) != 2:
                raise ConfigurationError("Checkstyle is probably not set up properly\n" + line)

            prefix, string = splitted[0], splitted[1]
            if prefix == "package":
                package_list.append(string.strip())
            elif prefix == "import":
                import_list.append(string.strip())
            elif prefix == "class":
                class_list.append(string.strip())
            elif prefix == "enum":
                enum_list.append(string.strip())
            elif prefix == "method":
                method_list.append(string.strip())
            else:
                raise ConfigurationError("Checkstyle is probably not set up properly\n" + line)
        '''
        if filenames:
            filename_defaults = {
                'accept' : 'The file ${name} is accepted',
                'warn' : "The file ${name} is unexpected",
                'reject' : "The file ${name} is unexpected",
                'require' : "The file ${pattern} is expected but missing",
                'expect' : "The file ${pattern} is expected but missing"
            }
            match(checker, "Filename check", file_list, filenames, filename_defaults)

        if packages:
            package_defaults = {
                'accept' : 'Package ${name} accepted',
                'warn' : "Package ${name} is unexpected",
                'reject' : "Package ${name} is unexpected",
                'require' : "Package ${pattern} is expected but missing",
                'expect' : "Package ${pattern} is expected but missing"
            }
            description = ""
            match(checker, "Package check", package_list, packages, package_defaults)

        if imports:
            import_defaults = {
                'accept' : 'Import from ${name} accepted',
                'warn' : "Import from ${name} is unexpected",
                'reject' : "Import from ${name} is unexpected",
                'require' : "Import from ${pattern} is expected but missing",
                'expect' : "Import from ${pattern} is expected but missing"
            }
            match(checker, "Import check", import_list, imports, import_defaults)

        if classes:
            classes_defaults = {
                'accept' : 'Class ${name} accepted',
                'warn' : "Class ${name} is unexpected",
                'reject' : "Class ${name} is unexpected",
                'require' : "Class ${pattern} is expected but missing",
                'expect' : "Class ${pattern} is expected but missing"
            }
            match(checker, "Class check", class_list, classes, classes_defaults)

        if enums:
            enum_defaults = {
                'accept' : 'Enum ${name} accepted',
                'warn' : "Enum ${name} is unexpected",
                'reject' : "Enum ${name} is unexpected",
                'require' : "Enum ${pattern} is expected but missing",
                'expect' : "Enum ${pattern} is expected but missing"
            }
            match(checker, "Enum check", enum_list, enums, enum_defaults)

        if methods:
            methods_defaults = {
                'accept' : 'Method ${name} accepted',
                'warn' : "Method ${name} is unexpected",
                'reject' : "Method ${name} is unexpected",
                'require' : "Method ${pattern} is expected but missing",
                'expect' : "Method ${pattern} is expected but missing"
            }
            match(checker, "Method check", method_list, methods, methods_defaults)

    return checker.result
