(defproject similar-set/similar-set "0.1.1"
  :description "Set operations where member matching is established with a supplied predicate"
  :url "http://github.com/judepayne/similar-set"
  :scm {:name "git"
        :url "http://github.com/judepayne/similar-set"}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :signing {:gpg-key "5C92FAF1"}
  :deploy-repositories [["clojars" {:creds :gpg}]]
  )
