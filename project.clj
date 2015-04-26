(defproject so_long_ow2 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "?"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot so-long-ow2.core
  :target-path "target/%s"
  :plugins [[lein-marginalia "0.8.0"]]
  :profiles {:uberjar {:aot :all}})
