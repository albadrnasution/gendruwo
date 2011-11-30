(defrule default
   (not (solution found))
   =>
   (assert (edibility poisonous))
   (assert (solution found))
)
(defrule poisonous
   (solution found)
   (edibility poisonous)
   =>
   (printout t "p" crlf)
)

(defrule edible
   (solution found)
   (edibility edible)
   =>
   (printout t "e" crlf)
)
