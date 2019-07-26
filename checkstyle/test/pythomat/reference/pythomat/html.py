"""Classes for writing well-formed HTML output.

This module provides classes for writing well-formed HTML.
The code in this module is intended to be used with context manages and
python's with statement::

   with document.h1():
      document.cdata("Title")
   with document.div():
      document.cdata("Regular text, ")
      with document.div({'class' : 'foobar'}):
         document.cdata("and foobar styled text.")

"""
import cgi


class HTMLException(Exception):
    """Exception to indicate something went wrong when rendering HTML output."""
    def __init__(self, message):
        super(Exception, self).__init__(message)


class _Tag(object):
    def __init__(self, document, name, attributes={}):
        # TODO: use argument packing/unpacking instead. at least if possible.
        self.document = document
        self.name = name
        self.attributes = attributes


    def __enter__(self):
        # TODO: make extra sure this doesn't fail: if __enter__ fails neither the with-body nor the __exit__ is executed
        self.open()
        return self


    def __exit__(self, type, value, traceback):
        self.close()

        # make sure we don't accidentaly use the Document after __exit__
        # was called
        del self.document

        return False


    def open(self):
        # first print it, then push it, otherwise, if printing fails we try to
        # pop, and therefore close, something that didn't get printed and
        # therefore was never opened.
        self.document.stream.write("<" + self.name + "".join([" " + str(key) + '="' + str(value) + '"' for key, value in self.attributes.items()]) + ">")
        self.document.stack.append(self)


    def close(self):
        self.document.stream.write("</" + self.name + ">")
        if self.document.stack[-1] != self:
            raise HTMLException("Trying to close tag which is not the last opened")

        self.document.stack.pop()


class Document(object):
    """Represents a well-formed HTML document or a well-formed fagment thereof.
    Example of a document with title "Page title" and content "Page content" in
    a div-tag::

       with html.Document(sys.stdout) as document:
          with document.html():
             with document.head():
                with document.title("Page title")
             with document.body():
                with document.div():
                    with document.cdata("Page content")

    """
    def __init__(self, stream):
        self.stream = stream
        self.stack = []


    def __enter__(self):
        return self


    def __exit__(self, type, value, traceback):
        try:
            if len(self.stack) != 0:
                while len(self.stack) != 0:
                    self.stack[-1].close()

                raise HTMLException("Not all opened tags closed at end of document")

            return False

        finally:
            # make sure we don't accidentaly use the Document after __exit__
            # was called.
            del self.stream


    def raw(self, string):
        """Write a string to the HTML document unescaped.

        For most cases you'll want to use cdata for security reasons.
        """
        self.stream.write(string)

    def cdata(self, string):
        """Write a string to the HTML document.

        The string is escaped, so < and > become HTML entities.
        """
        if not isinstance(string, str) and not isinstance(string, unicode):
            raise HTMLException("cannot use cdata with a non-string object")
        self.stream.write(cgi.escape(string))

    def pdata(self, string):
        """Write a string to the HTML document.

        The string is escaped. Additionally spaces are turned into non-breaking
        spaces."""
        if not isinstance(string, str) and not isinstance(string, unicode):
            raise HTMLException("cannot use cdata with a non-string object")
        self.stream.write(cgi.escape(string).replace(" ", "&nbsp;"))


    def __tag(self, name, attributes = {}):
        # return a tag object, which is "with-as"-compatible
        return _Tag(self, name, attributes)


    def html(self, attributes = {}):
        """Create a new html tag."""
        return self.__tag("html", attributes)

    def body(self, attributes = {}):
        """Create a new body tag."""
        return self.__tag("body", attributes)

    def head(self, attributes = {}):
        """Create a new head tag."""
        return self.__tag("head", attributes)

    def title(self, attributes = {}):
        return self.__tag("title", attributes)

    def meta(self, attributes = {}):
        return self.__tag("meta", attributes)

    def h1(self, attributes = {}):
        return self.__tag("h1", attributes)

    def h2(self, attributes = {}):
        return self.__tag("h2", attributes)

    def h3(self, attributes = {}):
        return self.__tag("h3", attributes)

    def h4(self, attributes = {}):
        return self.__tag("h4", attributes)

    def h5(self, attributes = {}):
        return self.__tag("h5", attributes)

    def h6(self, attributes = {}):
        return self.__tag("h6", attributes)

    def div(self, attributes = {}):
        """Create a new div-tag."""
        return self.__tag("div", attributes)

    def span(self, attributes = {}):
        """Create a new span-tag."""
        return self.__tag("span", attributes)

    def a(self, attributes = {}):
        return self.__tag("a", attributes)

    def font(self, attributes = {}):
        return self.__tag("font", attributes)

    def code(self, attributes = {}):
        return self.__tag("code", attributes)

    def pre(self, attributes = {}):
        return self.__tag("pre", attributes)

    def table(self, attributes = {}):
        return self.__tag("table", attributes)

    def colgroup(self, attributes = {}):
        return self.__tag("colgroup", attributes)

    def col(self, attributes = {}):
        with self.__tag("col", attributes = attributes):
            pass

    def thead(self, attributes = {}):
        return self.__tag("thead", attributes)

    def tfoot(self, attributes = {}):
        return self.__tag("tfoot", attributes)

    def th(self, attributes = {}):
        return self.__tag("th", attributes)

    def tr(self, attributes = {}):
        return self.__tag("tr", attributes)

    def td(self, attributes = {}):
        return self.__tag("td", attributes)

    def script(self, attributes={}):
        with self.__tag("script", attributes):
            pass

    def javascript(self):
        return self.__tag("script", {"type" : "text/javascript"})

    def css(self, style, attributes = {}):
        attributes['type'] = "text/css";
        with self.__tag("style", attributes):
            self.raw("<!--\n" + style + "\n-->")

    def flush(self):
        """Flush the underlying stream."""
        self.stream.flush()
