(ns checkers.ui
  (:require [checkers.db :as db]))

(def state db/state)

(def viewBox "0 0 320 320")

(def width 400)
(def height 400)
(def size 40)

(def white-cell-color "#C0C0C0")
(def black-cell-color "#303030")
(def white-checker-color "yellow")
(def black-checker-color "red")

(defn get-cell-color [cell]
  (if (= (:color cell) :white)
    white-cell-color
    black-cell-color))

(defn get-checker-color [cell]
  (if (= (:checker cell) :white)
    white-checker-color
    black-checker-color))

(defn get-by-component [component]
  (let [dataset (-> component .-target .-dataset)
        i (js/parseInt (aget dataset "i"))
        j (js/parseInt (aget dataset "j"))]
    [i j]))

(defn draw-desk [args]
  (let [{select-cell :on-select-cell
         unselect-cell :on-unselect-cell} args]
    (doall
     (for [pos (keys (sort-by first (:game @state)))
           :let [[i j] pos
                 cell (get-in @state [:game pos])
                 is-selected (not (empty? (filterv #(= % pos) (:steps @state))))]]
       ^{:key (str "cell_" i "_" j)}
       [:g
        (if is-selected
          [:rect {:x (+ (* j size) 2)
                  :y (+ (* i size) 2)
                  :width (- size 4)
                  :height (- size 4)
                  :fill (get-cell-color cell)
                  :data-i i
                  :data-j j
                  :stroke "blue"
                  :stroke-width 4
                  :on-click #(unselect-cell (get-by-component %))}]
          [:rect {:x (* j size)
                  :y (* i size)
                  :width size
                  :height size
                  :fill (get-cell-color cell)
                  :data-i i
                  :data-j j
                  :on-click #(select-cell (get-by-component %))}])]))))

(defn draw-checkers []
  (doall
   (for [pos (keys (sort-by first (:game @state)))
         :let [[i j] pos
               cell (get-in @state [:game pos])]]
     (when (:checker cell)
       ^{:key (str "checker_" i "_" j)}
       [:g {:style {:pointer-events "none"}}
        [:circle {:cx (+ (* j size) 20)
                  :cy (+ (* i size) 20)
                  :r 15
                  :fill (get-checker-color cell)}]]))))

(defn root [args]
  [:div
   [:button {:on-click db/reset} "Checkers - New Game"]
   [:span " "]
   [:b "turn: " (:turn @state)]
   [:br]
   [:br]
   [:svg {:width width :height height :viewBox viewBox}
    [:g {:transform "scale(1,-1)translate(0,-320)"}
     (draw-desk args)
     (draw-checkers)]]])
