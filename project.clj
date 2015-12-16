(defproject so_long_ow2 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "?"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.trace "0.7.8"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [mysql/mysql-connector-java "5.1.6"]]
  :main ^:skip-aot so-long-ow2.core
  :target-path "target/%s"
  :plugins [[lein-marginalia "0.8.0"]]
  :profiles {:uberjar {:aot :all}})
