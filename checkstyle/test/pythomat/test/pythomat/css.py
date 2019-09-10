"""Provide methods for injecting css code into a web page."""

import string
import re

# get element by id first to see if it got already inserted (multiple checkers
# might cause this)
inject_css_template = string.Template("""
if (document.getElementById($ID) == null) {
  var headElement = document.getElementsByTagName("head")[0];
  var cssElement = document.createElement('style');
  cssElement.setAttribute('type', 'text/css');
  cssElement.setAttribute('id', $ID);
  cssElement.innerHTML = $CSS;
  headElement.appendChild(cssElement);
}
""")
# seems not to work on IE8, could possibly try
#   cssElement.setAttribute('text', $CSS);
# or
#   cssElement.text = $CSS
# instead of
#   cssElement.innerHTML = $CSS
# not sure which one works, though. Me no web guy...

__compress = re.compile("[\s]+")


def inject_css(document, id, css):
    """Write a script to the document for injection a style into the document.

    Take a fragment of css code, compress it by removing bogus whitespace and
    newlines and write a script to the document that injects the css code into
    the head section of the document.
    """
    id = '"' + id + '"'
    css = __compress.sub(" ", css).strip()
    css = '"' + css + '"'
    with document.javascript():
        # print a script to the document that inserts css code into the document
        document.raw(inject_css_template.substitute({'ID': id, 'CSS': css}))


def color_bang(color, percent):
    """Similiar to xcolors color!fraction"""
    if isinstance(color, int):
        result = 255 - ((255 - color) * percent // 100)
        return result
    else:
        return [color_bang(component, percent) for component in color]


def toHex(color):
    """ turn a tuple or list of integers into a single hexadecimal string"""
    return "#" + "".join([hex(component).lstrip("0x").zfill(2) for component in color])


kit_colors = {
    "KITblack": (0, 0, 0),
    "KITgreen": (0, 150, 130),
    "KITblue": (70, 100, 170),
    "KITpalegreen": (130, 190, 60),
    "KITyellow": (250, 230, 20),
    "KITorange": (220, 160, 30),
    "KITbrown": (160, 130, 50),
    "KITred": (160, 30, 40),
    "KITlilac": (160, 0, 120),
    "KITcyanblue": (80, 170, 230),
    "KITseablue": (50, 80, 140),
}

extended_kit_colors = {}
for color, rgb in list(kit_colors.items()):
    extended_kit_colors[color] = toHex(rgb)
    for percent in [70, 50, 30, 15]:
        extended_kit_colors[color + str(percent)] = toHex(color_bang(rgb, percent))


def substitute_kit_colors(css):
    return string.Template(css).substitute(extended_kit_colors)


# default pythomat css code
__pythomat_css = """
.pythomat a
{
    text-decoration: inherit;
    color: inherit;
}

.pythomat .outer
{
    border-style: solid;
    border-color: $KITblack50;
    border-width: 1px;
    border-radius: 10px;
    margin: 10px 0px 10px 0px;
}

.pythomat .insetshadow
{
    -webkit-box-shadow: inset 0 0 4px rgba(0, 0, 0, 0.5);
    -moz-box-shadow: inset 0 0 4px rgba(0, 0, 0, 0.5);
    box-shadow: inset 0 0 4px rgba(0, 0, 0, 0.5);
}

.pythomat .showhide
{
    width: 2em;
    text-align: center;
    cursor: pointer;
    display: inline-block;
}

.pythomat h4
{
    background-color: $KITblack70;
    background: -moz-linear-gradient(top, $KITblack70, $KITblack);
    background: -webkit-linear-gradient(top, $KITblack70, $KITblack);
    background: -o-linear-gradient(top, $KITblack70, $KITblack);
    background: -ms-linear-gradient(top, $KITblack70, $KITblack);
    background: linear-gradient(top, $KITblack70, $KITblack);
    color: white;
    border-radius: 9px 9px 0px 0px;
    padding: 2px 10px 2px 10px;
    margin: 0px;
}

.pythomat .h5
{
    color: $KITblack70;
    font-weight: bold;
}

.pythomat .section
{
    margin: 20px;
}

.pythomat .inner
{
    padding: 10px;
}

.pythomat .severity .success
{
    background-color: $KITpalegreen;
    background: -moz-linear-gradient(top, $KITpalegreen70, $KITpalegreen);
    background: -webkit-linear-gradient(top, $KITpalegreen70, $KITpalegreen);
    background: -o-linear-gradient(top, $KITpalegreen70, $KITpalegreen);
    background: -ms-linear-gradient(top, $KITpalegreen70, $KITpalegreen);
    background: linear-gradient(top, $KITpalegreen70, $KITpalegreen);
}

.pythomat .severity .info
{
    background-color: $KITblue;
    background: -moz-linear-gradient(top, $KITblue70, $KITblue);
    background: -webkit-linear-gradient(top, $KITblue70, $KITblue);
    background: -o-linear-gradient(top, $KITblue70, $KITblue);
    background: -ms-linear-gradient(top, $KITblue70, $KITblue);
    background: linear-gradient(top, $KITblue70, $KITblue);
}

.pythomat .severity .warning
{
    background-color: $KITorange;
    background: -moz-linear-gradient(top, $KITorange70, $KITorange);
    background: -webkit-linear-gradient(top, $KITorange70, $KITorange);
    background: -o-linear-gradient(top, $KITorange70, $KITorange);
    background: -ms-linear-gradient(top, $KITorange70, $KITorange);
    background: linear-gradient(top, $KITorange70, $KITorange);
}

.pythomat .severity .failure
{
    background-color: $KITred;
    background: -moz-linear-gradient(top, $KITred70, $KITred);
    background: -webkit-linear-gradient(top, $KITred70, $KITred);
    background: -o-linear-gradient(top, $KITred70, $KITred);
    background: -ms-linear-gradient(top, $KITred70, $KITred);
    background: linear-gradient(top, $KITred70, $KITred);
}

.pythomat .severity .crash
{
    background-color: $KITlilac;
    background: -moz-linear-gradient(top, $KITlilac70, $KITlilac);
    background: -webkit-linear-gradient(top, $KITlilac70, $KITlilac);
    background: -o-linear-gradient(top, $KITlilac70, $KITlilac);
    background: -ms-linear-gradient(top, $KITlilac70, $KITlilac);
    background: linear-gradient(top, $KITlilac70, $KITlilac);
}

.pythomat .result
{
    border-radius: 0px 0px 9px 9px;
    color: white;
    padding: 2px 10px 2px 10px;
}

.pythomat .crashexplanation
{
    background-color: $KITlilac50;
    font-family:monospace;
    white-space:pre;
    padding: 2px 10px 2px 10px;
}

.pythomat .progressbar *
{
    color: white;
    overflow: hidden;
    white-space: nowrap;
    text-align: center;
    display: inline-block;
    padding: 2px 0 2px 0;
}

.pythomat .progressbar .first
{
    border-top-left-radius: 9px;
    border-bottom-left-radius: 9px;
}

.pythomat .progressbar .last
{
    border-top-right-radius: 9px;
    border-bottom-right-radius: 9px;
}

.pythomat .message .success
{
    color: $KITpalegreen;
}

.pythomat .message .info
{
    color: $KITblue;
}

.pythomat .message .warning
{
    color: $KITorange;
}

.pythomat .message .failure
{
    color: $KITred;
}

.pythomat .message .crash
{
    color: $KITlilac;
}

.pythomat .location
{
    font-weight: bold;
}

.pythomat .window
{
    margin: 20px auto;
    border-radius: 10px 10px 10px 10px;
    -webkit-box-shadow: 0 0 4px rgba(0, 0, 0, 0.5);
    -moz-box-shadow: 0 0 4px rgba(0, 0, 0, 0.5);
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.5);
}

.pythomat .window .decoration
{
    background-color: $KITblack30;
    background: -moz-linear-gradient(top, white, $KITblack30);
    background: -webkit-linear-gradient(top, white, $KITblack30);
    background: -o-linear-gradient(top, white, $KITblack30);
    background: -ms-linear-gradient(top, white, $KITblack30);
    background: linear-gradient(top, white, $KITblack30);
    border-style: solid;
    border-color: $KITblack70;
    color: $KITblack70;
}

.pythomat .window .title
{
    border-width: 1px 1px 0px 1px;
    border-radius: 10px 10px 0px 0px;
    font-weight:bold;
    padding: 5px 10px;
}

.pythomat .window .status
{
    border-width: 0px 1px 1px 1px;
    border-radius: 0px 0px 10px 10px;
    padding: 5px 10px;
}

.pythomat .console
{
    background-color: #1A1A1A;
    overflow: auto;
    resize: vertical;
    height: 400px;
    min-height: 200px;
    max-height: 800px
}

.pythomat .console *
{
    font-family: monospace;
    white-space: pre;
    color: white;
}

.pythomat .console .annotation
{
    margin: 2pt;
    padding: 2pt;
    border-radius: 2pt;
    display: inline-block;
}

.pythomat .console .prompt
{
    color: $KITblack30;
}

.pythomat .console .stdin
{
    color: $KITpalegreen70;
}

.pythomat .console .stdout
{
    color: $KITblue70;
}

.pythomat .console .stderr
{
    color: $KITred70;
}

.pythomat .console .skipped
{
    color: $KITblack50;
}

.pythomat .legend
{
    text-align: center;
}

.pythomat .legend *
{
    vertical-align : middle;
}

.pythomat .legend .box
{
    padding: 5px;
    margin: 10px;
    margin-left: 30px;
    border-style: solid;
    border-color: $KITblack70;
    border-width: 1px;
    font-size: 0px;
}

.pythomat .legend .stdin
{
    background-color: $KITpalegreen70;
}

.pythomat .legend .stdout
{
    background-color: $KITblue70;
}

.pythomat .legend .stderr
{
    background-color: $KITred70;
}

.pythomat .paper
{
    position: relative;
    max-width: 650px;
    padding: 50px;
    margin: 20px auto;
    background-color: #fff;
    -webkit-box-shadow: 0 0 4px rgba(0, 0, 0, 0.3), inset 0 0 50px rgba(0, 0, 0, 0.1);
    -moz-box-shadow: 0 0 4px rgba(0, 0, 0, 0.3), inset 0 0 50px rgba(0, 0, 0, 0.1);
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.3), inset 0 0 50px rgba(0, 0, 0, 0.1);
}

.pythomat .paper *
{
    font-family: monospace;
}

.pythomat .paper:before, .pythomat .paper:after
{
    position: absolute;
    width: 40%;
    height: 10px;
    content: ' ';
    left: 12px;
    bottom: 14px;
    background: transparent;
    -webkit-transform: skew(-5deg) rotate(-5deg);
    -moz-transform: skew(-5deg) rotate(-5deg);
    -ms-transform: skew(-5deg) rotate(-5deg);
    -o-transform: skew(-5deg) rotate(-5deg);
    transform: skew(-5deg) rotate(-5deg);
    -webkit-box-shadow: 0 6px 12px rgba(0, 0, 0, 0.5);
    -moz-box-shadow: 0 6px 12px rgba(0, 0, 0, 0.5);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.5);
    z-index: -1;
} 

.pythomat .paper:after
{
    left: auto;
    right: 12px;
    -webkit-transform: skew(5deg) rotate(5deg);
    -moz-transform: skew(5deg) rotate(5deg);
    -ms-transform: skew(5deg) rotate(5deg);
    -o-transform: skew(5deg) rotate(5deg);
    transform: skew(5deg) rotate(5deg);
}"""

__pythomat_css = substitute_kit_colors(__pythomat_css)


def inject_pythomat_css(document):
    """Inject the default pythomat css declarations into the document."""
    inject_css(document, "pythomat", __pythomat_css)
