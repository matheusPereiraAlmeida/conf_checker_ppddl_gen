(define (problem Align) (:domain Mining)
(:objects
a - node
b - node
c - node
d - node
modelEnd - node
ev1 - event
ev2 - event
ev3 - event
evEND - event)

(:init
(tracePointer ev1)
(activity a))

(:goal
(and
(not (activity a))
(not (activity b))
(not (activity c))
(not (activity d))
(activity modelEnd)
(tracePointer evEND)))

(:metric maximize (reward))
)
