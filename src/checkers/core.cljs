(ns checkers.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [checkers.db :as db]
            [checkers.ui :as ui]
            [clojure.core.async :as async]))

(defonce queue (async/chan))

(def state db/state)

(defn get-opponent [checker]
  (println checker)
  (if (= :white checker)
    :black
    :white)

  )

(defn my-range [from to]
  (if (> from to)
    (reverse
     (range to (inc from)))
    (range from (inc to))))

(defn remove-checkers [state steps]
  (let [paths (partition 2 1 steps)
        points-by-paths (for [[[i1 j1] [i2 j2]] paths]
                          (interleave (my-range i1 i2) (my-range j1 j2)))
        points (->> points-by-paths
                    (flatten)
                    (partition 2)
                    (mapv vec))]
    (reduce (fn [prev pos]
              (update-in prev [:game pos] dissoc :checker)) state points)))

(defn finish-turn [state opponent-checker]
  (if false
    (assoc state :turn opponent-checker)
    state))
  
(defn handle-message-move [state message]
  (let [{steps :steps} message
        from (first steps)
        from-cell (get-in state [:game from])
        opponent-checker (get-opponent (:checker from-cell))
        to (last steps)
        to-cell (get-in state [:game to])]
    (cond
      (and (from to (not= from to)))
      (-> state
          (assoc :steps steps)                    
          (remove-checkers steps)
          (assoc-in [:game to :checker] (:checker from-cell))
          (finish-turn opponent-checker))
      (and from)
      (assoc state :steps steps))))

(defn handle-message [message]
  (let [{type :type} message]
    (cond
      (= type :move)
      (swap! state handle-message-move message)
      :else
      (println "unknown action type: " type))))

(async/go-loop []
  (let [message (async/<! queue)]
    (handle-message message)
    (recur)))

(defn send-message [message]
  (async/go
    (async/>! queue message)))

(defn get-by-component [component]
  (let [dataset (-> component .-target .-dataset)
        i (js/parseInt (aget dataset "i"))
        j (js/parseInt (aget dataset "j"))]
    [i j]))

(defn is-empty-cell [pos]
  (let [cell (get-in @state [:game pos])
        is-empty (not (:checker cell))]
    is-empty))

(defn is-your-checker [pos]
  (let [turn (:turn @state)
        cell (get-in @state [:game pos])]
    (= turn (:checker cell))))

(def is-opponent-checker (complement is-your-checker))

(defn up [c]
  (if
   (= :white (:turn @state)) c
   (- c)))

(defn inc2 [num]
  (+ num 2))

(defn dec2 [num]
  (- num 2))

(defn is-allow-cell [pos1 pos2]
  (= pos1 pos2))

(defn select-cell [pos]
  (let [is-first-step (or
                       (empty? (:steps @state))
                       (= 2 (count (:steps @state))))
        is-second-step (= 1 (count (:steps @state)))]
    (cond
      (and
       is-first-step
       (is-your-checker pos))
      (send-message {:type :move :steps [pos]})

      (and
       is-second-step
       (is-empty-cell pos)
       (let [[i j] (first (:steps @state))]
         (or
          (and
           (is-opponent-checker [(inc i) (inc j)])
           (is-allow-cell pos [(inc2 i) (inc2 j)]))

          (and
           (is-opponent-checker [(inc i) (dec j)])
           (is-allow-cell pos [(inc2 i) (dec2 j)]))

          (and
           (is-opponent-checker [(dec i) (dec j)])
           (is-allow-cell pos [(dec2 i) (dec2 j)]))

          (and
           (is-opponent-checker [(dec i) (inc j)])
           (is-allow-cell pos [(dec2 i) (inc2 j)]))

          (is-allow-cell pos [(+ i (up 1)) (- j (up 1))])
          (is-allow-cell pos [(+ i (up 1)) (+ j (up 1))]))))

      (send-message {:type :move :steps [(first (:steps @state)) pos]}))))

(defn unselect-cell [pos]
  (swap! state (fn [prev-state]
                 (let [steps (:steps prev-state)
                       new-steps (filterv #(not= % pos) steps)
                       new-state (assoc prev-state :steps new-steps)]
                   new-state))))

(defn start []
  (rdom/render [ui/root {:on-select-cell select-cell
                         :on-unselect-cell unselect-cell}]
               (js/document.getElementById "app")))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (start))

(defn stop []
  ;; stop is called before any code is reloaded
  ;; this is controlled by :before-load in the config
  (js/console.log "stop"))
