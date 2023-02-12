(define (domain Mining)
(:requirements :typing :negative-preconditions :conditional-effects :probabilistic-effects :rewards)
(:types node event)

(:predicates
(activity ?p - node)
(tracePointer ?e - event)
)

(:action moveSync#a#ev1
:precondition (and  (activity a) (tracePointer ev1))
:effect (and (not (tracePointer ev1)) (tracePointer ev2)(probabilistic 1.0 (and (not (activity a)) (activity 1.0))) 
))

(:action moveInTheModel#a
:precondition (activity a)
:effect (probabilistic 1.0 (and (not (activity a)) (activity b) (decrease (reward) 1)) 
))

(:action moveSync#b#ev2
:precondition (and  (activity b) (tracePointer ev2))
:effect (and (not (tracePointer ev2)) (tracePointer ev3)(probabilistic 0.8384491114701131 (and (not (activity b)) (activity 0.8384491114701131))) 
0.13731825525040386 (and (not (activity b)) (activity 0.13731825525040386))) 
0.024232633279483037 (and (not (activity b)) (activity 0.024232633279483037))) 
))

(:action moveSync#b#ev3
:precondition (and  (activity b) (tracePointer ev3))
:effect (and (not (tracePointer ev3)) (tracePointer ev4)(probabilistic 0.8384491114701131 (and (not (activity b)) (activity 0.8384491114701131))) 
0.13731825525040386 (and (not (activity b)) (activity 0.13731825525040386))) 
0.024232633279483037 (and (not (activity b)) (activity 0.024232633279483037))) 
))

(:action moveSync#b#ev4
:precondition (and  (activity b) (tracePointer ev4))
:effect (and (not (tracePointer ev4)) (tracePointer ev5)(probabilistic 0.8384491114701131 (and (not (activity b)) (activity 0.8384491114701131))) 
0.13731825525040386 (and (not (activity b)) (activity 0.13731825525040386))) 
0.024232633279483037 (and (not (activity b)) (activity 0.024232633279483037))) 
))

(:action moveSync#b#ev5
:precondition (and  (activity b) (tracePointer ev5))
:effect (and (not (tracePointer ev5)) (tracePointer ev6)(probabilistic 0.8384491114701131 (and (not (activity b)) (activity 0.8384491114701131))) 
0.13731825525040386 (and (not (activity b)) (activity 0.13731825525040386))) 
0.024232633279483037 (and (not (activity b)) (activity 0.024232633279483037))) 
))

(:action moveSync#b#ev6
:precondition (and  (activity b) (tracePointer ev6))
:effect (and (not (tracePointer ev6)) (tracePointer ev7)(probabilistic 0.8384491114701131 (and (not (activity b)) (activity 0.8384491114701131))) 
0.13731825525040386 (and (not (activity b)) (activity 0.13731825525040386))) 
0.024232633279483037 (and (not (activity b)) (activity 0.024232633279483037))) 
))

(:action moveSync#b#ev7
:precondition (and  (activity b) (tracePointer ev7))
:effect (and (not (tracePointer ev7)) (tracePointer ev8)(probabilistic 0.8384491114701131 (and (not (activity b)) (activity 0.8384491114701131))) 
0.13731825525040386 (and (not (activity b)) (activity 0.13731825525040386))) 
0.024232633279483037 (and (not (activity b)) (activity 0.024232633279483037))) 
))

(:action moveSync#b#ev8
:precondition (and  (activity b) (tracePointer ev8))
:effect (and (not (tracePointer ev8)) (tracePointer ev9)(probabilistic 0.8384491114701131 (and (not (activity b)) (activity 0.8384491114701131))) 
0.13731825525040386 (and (not (activity b)) (activity 0.13731825525040386))) 
0.024232633279483037 (and (not (activity b)) (activity 0.024232633279483037))) 
))

(:action moveInTheModel#b
:precondition (activity b)
:effect (probabilistic 0.8384491114701131 (and (not (activity b)) (activity b) (decrease (reward) 1)) 
0.13731825525040386 (and (not (activity b)) (activity c) (decrease (reward) 1)) 
0.024232633279483037 (and (not (activity b)) (activity d) (decrease (reward) 1)) 
))

(:action moveSync#c#ev9
:precondition (and  (activity c) (tracePointer ev9))
:effect (and (not (tracePointer ev9)) (tracePointer evEND)(probabilistic 1.0 (and (not (activity c)) (activity 1.0))) 
))

(:action moveInTheModel#c
:precondition (activity c)
:effect (probabilistic 1.0 (and (not (activity c)) (activity modelend) (decrease (reward) 1)) 
))

(:action moveInTheModel#d
:precondition (activity d)
:effect (probabilistic 1.0 (and (not (activity d)) (activity modelend) (decrease (reward) 1)) 
))

(:action moveInTheLog#a#ev1-ev2
:precondition (tracePointer ev1)
:effect (and (not (tracePointer ev1)) (tracePointer ev2) (decrease (reward) 1) ))

(:action moveInTheLog#b#ev2-ev3
:precondition (tracePointer ev2)
:effect (and (not (tracePointer ev2)) (tracePointer ev3) (decrease (reward) 1) ))

(:action moveInTheLog#b#ev3-ev4
:precondition (tracePointer ev3)
:effect (and (not (tracePointer ev3)) (tracePointer ev4) (decrease (reward) 1) ))

(:action moveInTheLog#b#ev4-ev5
:precondition (tracePointer ev4)
:effect (and (not (tracePointer ev4)) (tracePointer ev5) (decrease (reward) 1) ))

(:action moveInTheLog#b#ev5-ev6
:precondition (tracePointer ev5)
:effect (and (not (tracePointer ev5)) (tracePointer ev6) (decrease (reward) 1) ))

(:action moveInTheLog#b#ev6-ev7
:precondition (tracePointer ev6)
:effect (and (not (tracePointer ev6)) (tracePointer ev7) (decrease (reward) 1) ))

(:action moveInTheLog#b#ev7-ev8
:precondition (tracePointer ev7)
:effect (and (not (tracePointer ev7)) (tracePointer ev8) (decrease (reward) 1) ))

(:action moveInTheLog#b#ev8-ev9
:precondition (tracePointer ev8)
:effect (and (not (tracePointer ev8)) (tracePointer ev9) (decrease (reward) 1) ))

(:action moveInTheLog#c#ev9-evEND
:precondition (tracePointer ev9)
:effect (and (not (tracePointer ev9)) (tracePointer evEND) (decrease (reward) 1) ))

)