This directory contains design documents related to JSR 308 ("Annotations
on Java types").

Also see file README-jsr308.html .

To copy some documents to the website (but NOT THE SPECIFICATION!), run
  make web

To make a new release of the specification, on the website:
1. See the comments in the Makefile for instructions regarding how to
update the jsr308-changes.html file.
2. Run
  make web-spec
3. Check the results at
  http://types.cs.washington.edu/jsr308/
4. Send email to type-annotations-spec-experts@openjdk.java.net announcing
the release.  Include the relevant changelog entries (from jsr308-changes.html
or http://types.cs.washington.edu/jsr308/specification/jsr308-changes.html).

To update the repository to a newer version of the upstream OpenJDK, do either:
  hg fetch http://hg.openjdk.java.net/jdk8/tl/langtools
or:
  hg fetch http://hg.openjdk.java.net/jdk8/jdk8/langtools
They sometimes contain different changesets.

Also see the instructions at:
  ../../checker-framework/release/README-maintainers.html
