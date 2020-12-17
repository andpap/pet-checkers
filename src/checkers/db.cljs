(ns checkers.db
  (:require [reagent.core :as r]))

(def initial-state
  {:steps []
   :turn :white
   :game {[0 0] {:color :black
                 :checker :white}
          [0 1] {:color :white}
          [0 2] {:color :black
                 :checker :white}
          [0 3] {:color :white}
          [0 4] {:color :black
                 :checker :white}
          [0 5] {:color :white}
          [0 6] {:color :black
                 :checker :white}
          [0 7] {:color :white}
          [1 0] {:color :white}
          [1 1] {:color :black
                 :checker :white}
          [1 2] {:color :white}
          [1 3] {:color :black
                 :checker :white}
          [1 4] {:color :white}
          [1 5] {:color :black
                 :checker :white}
          [1 6] {:color :white}
          [1 7] {:color :black
                 :checker :white}
          [2 0] {:color :black
                 :checker :white}
          [2 1] {:color :white}
          [2 2] {:color :black
                 :checker :white}
          [2 3] {:color :white}
          [2 4] {:color :black
                 :checker :white}
          [2 5] {:color :white}
          [2 6] {:color :black
                 :checker :white}
          [2 7] {:color :white}
          [3 0] {:color :white}
          [3 1] {:color :black}
          [3 2] {:color :white}
          [3 3] {:color :black}
          [3 4] {:color :white}
          [3 5] {:color :black}
          [3 6] {:color :white}
          [3 7] {:color :black}
          [4 0] {:color :black}
          [4 1] {:color :white}
          [4 2] {:color :black}
          [4 3] {:color :white}
          [4 4] {:color :black}
          [4 5] {:color :white}
          [4 6] {:color :black}
          [4 7] {:color :white}
          [5 0] {:color :white}
          [5 1] {:color :black
                 :checker :black}
          [5 2] {:color :white}
          [5 3] {:color :black
                 :checker :black}
          [5 4] {:color :white}
          [5 5] {:color :black
                 :checker :black}
          [5 6] {:color :white}
          [5 7] {:color :black
                 :checker :black}
          [6 0] {:color :black
                 :checker :black}
          [6 1] {:color :white}
          [6 2] {:color :black
                 :checker :black}
          [6 3] {:color :white}
          [6 4] {:color :black
                 :checker :black}
          [6 5] {:color :white}
          [6 6] {:color :black
                 :checker :black}
          [6 7] {:color :white}
          [7 0] {:color :white}
          [7 1] {:color :black
                 :checker :black}
          [7 2] {:color :white}
          [7 3] {:color :black
                 :checker :black}
          [7 4] {:color :white}
          [7 5] {:color :black
                 :checker :black}
          [7 6] {:color :white}
          [7 7] {:color :black
                 :checker :black}}})

(defonce state (r/atom initial-state))

(defn reset []
  (reset! state initial-state))
