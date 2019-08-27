SHELL = /bin/bash

all: main.pdf

main.pdf: assignment.cls main.tex rules.tex header.tex
	pdflatex main.tex
	pdflatex main.tex

clean:
	rm -f *.aux *.log *.out main.synctex.gz

realclean: clean
	rm -f main.pdf

