# Makefile LaTex documents
#
# Autor: Gabriel Tamura
# Fecha Creaci'on: 2010-02-21
# Descripci'on:
#   Makefile para generar el postscript/pdf a partir del .tex
#
# Modificaciones:
# --------------
# gtamura, 2012-10-24: bbl processing requires the same latex processing command.
# Descripci'on:
#

SRC = 0-MIT-Proposal
SRCBIB = self-adaptation
TEXCMD = pdflatex
DEPEND =  $(SRCBIB).bbl *.tex

all: $(SRC).pdf
#all: $(SRC).dvi $(SRC).pdf

#$(SRC).pdf: $(SRC).dvi
#	dvipdf $(SRC).dvi

#$(SRC).dvi: $(DEPEND)
#	$(TEXCMD) $(SRC)
#	$(TEXCMD) $(SRC)

$(SRC).pdf: $(DEPEND)
	$(TEXCMD) $(SRC)
	$(TEXCMD) $(SRC)

$(SRC).ps: $(SRC).dvi
	dvips -o $(SRC).ps $(SRC).dvi
#	cat GLOBALVER > ../VER; cat ../STATUS  >> ../VER; date >> ../VER

$(SRCBIB).bbl: $(SRCBIB).bib
	$(TEXCMD) $(SRC)
	bibtex $(SRC)

clean:
	rm -f *.log *.aux *.toc *.bbl *.blg *~ *.lot *.lof *.backup $(SRC).dvi $(SRC).out $(SRC).ps $(SRC).pdf

# ----------------------- Fin De Archivo ----------------------- 
