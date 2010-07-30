jsr308_www ?= /cse/www2/types/jsr308
jsr308_spec = ${jsr308_www}/specification
jsr308_www_online ?= http://types.cs.washington.edu/jsr308

PLUME ?= /homes/gws/mernst/bin/share-plume
RELEASEDATE ?= `date '+%Y%m%d'`

# There is no use of "cp -p" in this Makefile, to avoid problems with
# browser caches.

local: design.pdf design.html type-quals.pdf type-quals.html validate

clean:
	rm -f design.aux design.blg design.dvi design.haux design.html design.htoc design.image.tex design.log design.pdf design.toc design001.gif design001.png figures/type-tree.eps figures/type-tree.pdf type-quals.aux type-quals.dvi type-quals.html type-quals.log type-quals.pdf

figures-all:
	${MAKE} -C figures all

html: design.html type-quals.html

# Check HTML
validate:
	validate jsr308-webpage.html jsr308-faq.html jsr308-changes.html

# Update the main webpage & FAQ only, but not any of the supporting documents.
# Useful to fix a minor typo (but then you might just want to edit both versions, identically).
webpage: ${jsr308_www}/jsr308-webpage.html ${jsr308_www}/jsr308-faq.html
#	$(MAKE) checklinks

# Warning!  "make web" makes a public release!
# Before doing this, update jsr308-changes.html.  Determine changes via log or diff:
#   hg log --style changelog --date '>2009-05-14' design.tex jsr308-faq.html
# where "2009-05-14" is the date of the previous entry in jsr308-changes.html
web: \
${jsr308_www} \
${jsr308_www}/jsr308-webpage.html \
${jsr308_www}/jsr308-faq.html \
${jsr308_www}/Makefile-jsr308-install \
${jsr308_spec} \
${jsr308_spec}/java-annotation-design.pdf \
${jsr308_spec}/java-annotation-design.html \
${jsr308_spec}/jsr308-changes.html \
${jsr308_www}/java-type-qualifiers.pdf \
${jsr308_www}/java-type-qualifiers.html
	\cp -f design.html ${jsr308_spec}/java-annotation-design-${RELEASEDATE}.html
	$(MAKE) checklinks

## There are some expected errors, but they should all be suppressed by
## the checklink-args.txt file, so this should complete cleanly.
# * The two errors related to 'annotation_type_element_detail' are the due to
#   a bug in the Javadoc generator when a default value is given to an
#   annotation element.
# * Others are a result of linking our javadocs to the Sun JDK
#   javadoc.  Any classes that are introduced by JSR 308 (e.g.
#   AnnotatedType tree) or affected by it (e.g. AnnotatedTypeVisitor,
#   AnnotatedTypeScanner, SourceVersion) report an error.  A fix is to
#   distribute our own Javadoc for the compiler.
checklinks:
	${PLUME}/checklink -q -r -e `cat ${PLUME}/checklink-args.txt` ${jsr308_www_online}
	${PLUME}/checklink -q -r -e `cat ${PLUME}/checklink-args.txt` ${jsr308_www_online}/../checker-framework


###########################################################################

design.dvi: design.tex
	$(MAKE) -C figures
	latex $<
	bibtex $(basename $<)
	perl -pi -e 's/%\n//' design.bbl
	latex $<

design.pdf: design.tex design.dvi
	pdflatex $<
	bibtex $(basename $<)
	perl -pi -e 's/%\n//' design.bbl
	pdflatex $<

design.html: design.tex design.dvi design.bbl
	hevea -fix -exec xxdate.exe urlhref.hva design.tex
	mv design.html design.html-with-htoc
	hevea-retarget-crossrefs < design.html-with-htoc > design.html
	rm -rf design.html-with-htoc
#	perl -pi -e 's/(be as clear.<\/P><OL )/$$1start="5" /' design.html
#	perl -pi -e 's/(<!--TOC section Introduction-->)/$$1\n\n<p>Contents:<\/p>\n<!-- start toc.  do not edit; run html-update-toc instead -->\n<!-- end toc -->\n\n<br>\n/' design.html
#	html-update-toc design.html

${jsr308_www}:
	mkdir ${jsr308_www}

${jsr308_spec}:
	mkdir ${jsr308_spec}

${jsr308_spec}/java-annotation-design.pdf: design.pdf
	\cp -f $< $@
	cd ${jsr308_www}; html-update-link-dates jsr308-webpage.html

${jsr308_spec}/java-annotation-design.html: design.html
	\cp -f $< $@
# We don't know which of the two files will be created
	-\cp -f design001.gif ${jsr308_spec}
	-\cp -f design001.png ${jsr308_spec}
	cd ${jsr308_www}; html-update-link-dates jsr308-webpage.html

###########################################################################

type-quals.dvi: type-quals.tex
	latex $<

type-quals.pdf: type-quals.tex type-quals.dvi
	pdflatex $<

type-quals.html: type-quals.tex type-quals.dvi
	hevea -exec xxdate.exe urlhref.hva type-quals.tex
	perl -pi -e 's/(<!--TOC section Introduction-->)/$$1\n\n<p>Contents:<\/p>\n<!-- start toc.  do not edit; run html-update-toc instead -->\n<!-- end toc -->\n\n<br>\n/' type-quals.html
	html-update-toc type-quals.html

${jsr308_www}/java-type-qualifiers.pdf: type-quals.pdf
	\cp -f $< $@
	cd ${jsr308_www}; html-update-link-dates jsr308-webpage.html

${jsr308_www}/java-type-qualifiers.html: type-quals.html
	\cp -f $< $@
	cd ${jsr308_www}; html-update-link-dates jsr308-webpage.html

###########################################################################

${jsr308_www}/jsr308-webpage.html: jsr308-webpage.html
	validate $<
	\cp -f $< $@
	cd ${jsr308_www}; html-update-link-dates jsr308-webpage.html

${jsr308_www}/jsr308-faq.html: jsr308-faq.html
	validate $<
	\cp -f $< $@
#	cd ${jsr308_www}; html-update-link-dates jsr308-webpage.html

${jsr308_spec}/jsr308-changes.html: jsr308-changes.html
	validate $<
	\cp -f $< $@
	cd ${jsr308_www}; html-update-link-dates jsr308-webpage.html

${jsr308_www}/Makefile-jsr308-install: Makefile-jsr308-install
	\cp -f $< $@

###########################################################################
