(ns rum.test.server
  (:require
   [rum.core :as rum]
   [clojure.test :refer [deftest is are testing]]))

(rum/defcs comp-mixins < (rum/local 7)
  {:will-mount (fn [s] (assoc s ::key 1))}
  [state]
  [:div
   [:.local @(:rum/local state)]
   [:.key   (::key state)]])

(deftest test-93
  (is (= (rum/render-static-markup [:a {:type "<script>"}])
         "<a type=\"&lt;script&gt;\"></a>"))
  (is (= (rum/render-static-markup [:a {:class "<script>"}])
         "<a class=\"&lt;script&gt;\"></a>")))

(deftest test-120
  (is (= (rum/render-static-markup [:input {:type :checkbox}])
         "<input type=\"checkbox\"/>")))

(rum/defc c-185 []
  (for [x (range 2)]
    [:b x]))

(deftest test-185
  (is (= (rum/render-static-markup (rum/with-key (c-185) "hello"))
         "<b>0</b><b>1</b>")))

(deftest test-suspense
  (is (= (rum/render-static-markup (rum/suspense {} [:span]))
         "<span></span>")))

(deftest test-fragment
  (is (= (rum/render-static-markup (rum/fragment [:span] [:div]))
         "<span></span><div></div>")))

(deftest test-lifecycle
  (is (= (comp-mixins)
         [:div
          [:.local 7]
          [:.key   1]])))

(rum/defc comp-arglists
  ([a])
  ([a b])
  ([a b c]))

(rum/defcc comp-arglists-1
  ([comp a])
  ([comp a b])
  ([comp a b c]))

(deftest test-arglists
  (is (= (:arglists (meta #'comp-mixins))
         '([])))
  (is (= (:arglists (meta #'comp-arglists))
         '([a] [a b] [a b c])))
  (is (= (:arglists (meta #'comp-arglists-1))
         '([a] [a b] [a b c]))))
