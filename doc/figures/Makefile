all: pdf eps
pdf: type-tree.pdf
eps: type-tree.eps

%.pdf : %.eps
	epstopdf $<

%.eps : %.dia
	dia -t eps $<

clean:
	@\rm *.eps
	@\rm *.pdf
