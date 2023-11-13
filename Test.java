.CODE
PUSH simple_heap_init
CALL
PUSH main@Init
CALL
HALT

simple_heap_init:
RET 0

simple_malloc:
LOADFP ; Inicialización unidad
LOADSP
STOREFP ; Finaliza inicialización del RA
LOADHL ; hl
DUP ; hl
PUSH 1 ; 1
ADD ; hl + 1
STORE 4 ; Guarda resultado (puntero a base del bloque)
LOAD 3 ; Carga cantidad de celdas a alojar (parámetro)
ADD
STOREHL ; Mueve el heap limit (hl)
STOREFP
RET 1 ; Retorna eliminando el parámetro

.DATA
VT_Init: NOP

.CODE
main@Init:
LOADFP
LOADSP
STOREFP
RMEM 1 ; guardo lugar para la variable local a
PUSH VT_System
POP ; como es estatico no me sirve la referencia anterior
PUSH main@Init; etiqueta
CALL
STORE -1
RMEM 1 ; guardo lugar para la variable local b
RMEM 1 ; reservo para el malloc
PUSH 01; apilo los atributos
PUSH simple_malloc ; rutina de heap
CALL
DUP ; no perder el nuevo CiR
PUSH VT_System; apilo el comienzo de la vt
STOREREF 0 
STORE -1
RMEM 1 ; guardo lugar para la variable local c
RMEM 1 ; reservo para el malloc
PUSH 01; apilo los atributos
PUSH simple_malloc ; rutina de heap
CALL
DUP ; no perder el nuevo CiR
PUSH VT_System; apilo el comienzo de la vt
STOREREF 0 
STORE -1
LOAD -1 ; apilo variable local
PUSH 5; apilo entero
LT
BF labelElsenull ; salta a la condicion else
LOAD -1 ; apilo variable local
LOADREF -1
PUSH 7; apilo entero
FMEM 0 ; libero el espacio para las variables locles, se liberaron 0espacios de memoria
JUMP labelThennull ; salteo al else
labelElsenull:
LOAD -1 ; apilo variable local
LOADREF -1
PUSH 5; apilo entero
FMEM 0 ; libero el espacio para las variables locles, se liberaron 0espacios de memoria
labelThennull: NOP
PUSH VT_System
POP ; como es estatico no me sirve la referencia anterior
LOAD -1 ; apilo variable local
LOADREF -1
PUSH main@Init; etiqueta
CALL
FMEM 3 ; libero el espacio para las variables locles, se liberaron 3espacios de memoria
STOREFP
RET 1



.DATA
VT_B: NOP

.CODE



.DATA
VT_C: NOP

.CODE



.DATA
VT_Object: NOP

.CODE
debugPrint$int@Object:
LOADFP
LOADSP
STOREFP
LOAD 3 ; cargo el primer parametro
IPRINT
PRNLN
STOREFP
RET 1; +1





.DATA
VT_String: NOP

.CODE



.DATA
VT_System: NOP

.CODE
printC$boolean@System:
LOADFP
LOADSP
STOREFP
LOAD 3 ; cargo el primer parametro
CPRINT
STOREFP
RET 1; +1


printS$string@System:
LOADFP
LOADSP
STOREFP
LOAD 3 ; cargo el primer parametro
SPRINT
STOREFP
RET 1; +1


println@System:
LOADFP
LOADSP
STOREFP
PRNLN
STOREFP
RET 0; +0


printCln$char@System:
LOADFP
LOADSP
STOREFP
LOAD 3 ; cargo el primer parametro
CPRINT
PRNLN
STOREFP
RET 1; +1


printSln$string@System:
LOADFP
LOADSP
STOREFP
LOAD 3 ; cargo el primer parametro
SPRINT
PRNLN
STOREFP
RET 1; +1


read@System:
LOADFP
LOADSP
STOREFP
READ ; lectura de un digito entero
PUSH 48 ; apilo el 48 para restarselo al ASCII del digito paa obtener el numero
SUB
STORE 3 ; pongo el tope de la pila en la locacion reservada
STOREFP
RET 0; +0


printB$boolean@System:
LOADFP
LOADSP
STOREFP
LOAD 3 ; cargo el primer parametro
BPRINT
STOREFP
RET 1; +1


printIln$int@System:
LOADFP
LOADSP
STOREFP
LOAD 3 ; cargo el primer parametro
IPRINT
PRNLN
STOREFP
RET 1; +1


printI$int@System:
LOADFP
LOADSP
STOREFP
LOAD 3 ; cargo el primer parametro
IPRINT
STOREFP
RET 1; +1


printBln$boolean@System:
LOADFP
LOADSP
STOREFP
LOAD 3 ; cargo el primer parametro
BPRINT
PRNLN
STOREFP
RET 1; +1





