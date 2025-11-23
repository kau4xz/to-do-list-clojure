(defproject todo-app "0.1.0-SNAPSHOT"
  :description "Todo List Fullstack com Clojure e ClojureScript"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [metosin/reitit "0.6.0"]
                 [ring/ring-jetty-adapter "1.9.6"]
                 [ring-cors "0.1.13"]
                 [com.github.seancorfield/next.jdbc "1.3.883"]
                 [org.xerial/sqlite-jdbc "3.42.0.0"]
                 [org.clojure/data.json "2.4.0"]]
  :main ^:skip-aot server
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})