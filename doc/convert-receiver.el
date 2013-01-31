;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Changing receiver annotation syntax for JSR 308
;;;

;; This is Emacs code that converts from old-style (C++-style) receiver
;; annotations, such as:
;;   class MyClass {
;;     void myMethod() /*@ReceiverAnno*/ { ... }
;;     void myMethod2(int x, bool y) /*@ReceiverAnno*/ { ... }
;;   }
;; to new-style receiver annotations on a formal parameter named "this", such as 
;;   class MyClass {
;;     void myMethod(@ReceiverAnno Myclass this) { ... }
;;     void myMethod2(@ReceiverAnno MyClass this, int x, bool y) { ... }
;;   }
;; It is not 100% automated, nor does it convert all code perfectly, but it
;; greatly reduces manual effort.
;; A Python script, convert-receiver.py, that performs this same action is
;; also available.


;; Start off with this as a compilation (not a grep!) command:
;;   search -i -n '[^"]\) (/\*)?@' | grep -v '\.jaif:'
;; (The search program is http://plume-lib.googlecode.com/hg/bin/search, or
;; you can use find and grep instead.)

;; Then create a macro that does
;;   C-x `           (next-error)
;;   M-x change-receiver-annotation-1 RET
;; and apply it repeatedly.

;; Which buffer is being operated on gets confused if (next-error) is
;; within this.
(defun change-receiver-annotation-1 ()
  "Change one JSR 308 receiver annotation from old style to new style.
Moves the annotation from after the parameter list (in C++ location) to
a new first parameter named \"this\"."
  (interactive)
  ;; 
  (let ((class-name (save-excursion
		      (re-search-backward "\\b\\(class\\|interface\\) \\([A-Za-z0-9]+\\(<[A-Za-z0-9]+\\(,[ \t\n]*[A-Za-z0-9]+\\)*>\\)?\\)[ \t\n]*\\({\\|[ \t\n]\\(implements\\|extends\\)\\)") ; regex must be improved!
		      (match-string 2))))
    (message "class-name %s" class-name)
    (re-search-forward ") \\(/\\*\\)?@")
    (goto-char (1+ (match-beginning 0)))
    (if (not (looking-at " \\(/\\*\\)?\\(@[A-Za-z0-9]+\\)\\(\\*/\\)?"))
	(error "Not looking at annotation"))
    (let ((incomment (match-string 1))
	  (annotation (match-string 2))
	  (incomment2 (match-string 3)))
      (assert (equal (null incomment) (null incomment2)))
      (delete-region (match-beginning 0) (match-end 0))
      (backward-sexp 1)
      (forward-char 1)
      (if incomment
	  (insert incomment))
      (insert annotation " " class-name " this")
      (let ((lastarg (looking-at ")")))
	(if (not lastarg)
	    (insert ","))
	(if incomment2
	    (insert incomment2))
	(if (not lastarg)
	    (insert " "))))))


