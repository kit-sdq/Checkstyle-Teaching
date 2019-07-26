allfiles = ["00_public.py",
            "checkstyle-optional.py",
            "checkstyle-required.py",
            "checkstyle-teaching-1.0.jar",
            "javalyzer.py",
            "pythomat.zip",
            ".idea\\\\checkstyle-idea.xml",
            ".idea\\\\deployment.xml",
            ".idea\\\\misc.xml",
            ".idea\\\\modules.xml",
            ".idea\\\\TaskC.iml",
            ".idea\\\\uiDesigner.xml",
            ".idea\\\\workspace.xml",
            ".idea\\\\inspectionProfiles\\\\Project_Default.xml",
            "copy\\\\code\\\\Terminal.java",
            "copy\\\\code\\\\_intern\\\\terminal\\\\Entry.java",
            "copy\\\\code\\\\_intern\\\\terminal\\\\Prefix.java",
            "copy\\\\code\\\\_intern\\\\terminal\\\\analyzer\\\\Analyzer.java",
            "copy\\\\code\\\\_intern\\\\terminal\\\\analyzer\\\\Line.java",
            "copy\\\\code\\_intern\\terminal\\analyzer\\Type.java",
            "copy\\code\\_intern\\terminal\\normalizer\\Normalizer.java",
            "copy\\code\\_intern\\terminal\\normalizer\\Type.java",
            "copy\\code\\_intern\\terminal\\parsing\\Info.java",
            "copy\\code\\_intern\\terminal\\parsing\\Parser.java",
            "copy\\code\\_intern\\util\\StringUtil.java",
            "copy\\code\\_intern\\util\\collections\\CNode.java",
            "copy\\code\\_intern\\util\\collections\\Node.java",
            "copy\\code\\_intern\\util\\invoke\\Conversion.java",
            "copy\\code\\_intern\\util\\invoke\\ConverterMethods.java",
            "copy\\code\\_intern\\util\\invoke\\InvocationException.java",
            "copy\\code\\_intern\\util\\invoke\\Invocations.java",
            "copy\\code\\_intern\\util\\invoke\\RatedConverterMethod.java",
            "copy\\code\\_intern\\util\\invoke\\UncheckedReflectiveOperationException.java",
            "copy\\code\\_intern\\util\\memoizer\\Computable.java",
            "copy\\code\\_intern\\util\\memoizer\\Memoizer.java",
            "copy\\code\\_intern\\util\\syntax\\Rule.java",
            "copy\\code\\_intern\\util\\syntax\\Syntax.java",
            "copy\\code\\_intern\\util\\syntax\\tokenizer\\Module.java",
            "copy\\code\\_intern\\util\\syntax\\tokenizer\\ModuleInformation.java",
            "copy\\code\\_intern\\util\\syntax\\tokenizer\\Token.java",
            "copy\\code\\_intern\\util\\syntax\\tokenizer\\Tokenizer.java",
            "copy\\code\\_intern\\util\\syntax\\tokenizer\\TokenizerException.java",
            "src\\edu\\kit\\informatik\\Color.java",
            "src\\edu\\kit\\informatik\\Command.java",
            "src\\edu\\kit\\informatik\\GameState.java",
            "src\\edu\\kit\\informatik\\InputException.java",
            "src\\edu\\kit\\informatik\\Main.java",
            "src\\edu\\kit\\informatik\\Mastermind.java",
            "src\\edu\\kit\\informatik\\Result.java",
            "src\\edu\\kit\\informatik\\Terminal.java"]

from pythomat import interactive
import sys

tests = [
    {
        'name': 'Public Test',
        'description': 'Tests if a simple scenario works correctly.',
        'protocol': 'public.txt',
        'arguments': 'input.txt',
        'files': {"input.txt": open('copy/input/pubtest.txt', 'r').read()},
    }
]

for test in tests:
    test['protocol'] = 'copy/protocols/public/' + test.get('protocol')

success = interactive.run(allfiles[1:], tests,
                          description="These are public tests. Remember, we have got a lot more than just these.", main="Main", java="C:\\Program Files\\Java\\jdk1.8.0_162\\bin\\java.exe")
sys.exit(0 if success else 1)
