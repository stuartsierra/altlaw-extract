(ns runtests
  (:use clojure.test))

(run-all-tests #"^org\.altlaw\..*$")
