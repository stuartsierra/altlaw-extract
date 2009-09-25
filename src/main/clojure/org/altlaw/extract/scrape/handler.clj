(ns org.altlaw.extract.scrape.handler
  (:require [org.altlaw.util.jruby :as ruby]
            [clojure.walk :as walk]))

(def #^{:private true} *scraper-handler*
     (delay
      (ruby/eval-jruby "require 'org/altlaw/extract/scrape/scraper_handler'")
      (ruby/eval-jruby "ScraperHandler.new")))

(defn scraper-handler []
  (force *scraper-handler*))

(defn run-scrapers [download]
  (assert (map? download))
  (assert (contains? download :request_uri))
  (map ruby/convert-jruby
       (ruby/eval-jruby "$handler.parse(Download.from_map($download))"
                        {:download (walk/stringify-keys download)
                         :handler (scraper-handler)})))

(defn all-requests []
  (map ruby/convert-jruby
       (ruby/eval-jruby "$handler.all_requests"
                        {:handler (scraper-handler)})))
