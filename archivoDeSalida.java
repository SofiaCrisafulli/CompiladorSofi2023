.CODE
PUSH simple_heap_init
CALL
PUSH main@Init
CALL
HALT

simple_heap_init: RET 0 ; Retorna inmediatamente

simple_malloc:
LOADFP ; Inicialización unidad
LOADSP
STOREFP ; Finaliza inicialización del RA
LOADHL ; hl
DUP ; hl
PUSH 1 ; 1
ADD ; hl + 1
STORE 4 ; Guarda el resultado (un puntero a la primer celda de la región de memoria)
LOAD 3 ; Carga cantidad de celdas a alojar (parámetro)
ADD
STOREHL ; Mueve el heap limit (hl)
STOREFP ; Almacena el tope de la pila en el registro fp
RET 1 ; Retorna eliminando el parámetro

.DATA
VT_Init: NOP

.CODE
main@Init:
LOADFP ; Apila el valor del registro fp
LOADSP ; Apila el valor del registro sp
STOREFP ; Almacena el tope de la pila en el registro fp
PUSH 1234 ; Apila el entero 1234
PUSH main@Init ; Apila el método 
CALL ; Llama al método en el tope de la pila
FMEM 0
STOREFP ;AF Almacena el tope de la pila en el registro fp
RET 0



.DATA
VT_Object: NOP

.CODE
debugPrint$int@Object:
LOADFP
LOADSP
STOREFP ; Almacena el tope de la pila en el registro fp
LOAD 3 ; cargo el primer parametro
IPRINT
PRNLN
STOREFP ; Almacena el tope de la pila en el registro fp
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
STOREFP ; Almacena el tope de la pila en el registro fp
LOAD 3 ; cargo el primer parametro
CPRINT
STOREFP ; Almacena el tope de la pila en el registro fp
RET 1; +1


printS$string@System:
LOADFP
LOADSP
STOREFP ; Almacena el tope de la pila en el registro fp
LOAD 3 ; cargo el primer parametro
SPRINT
STOREFP ; Almacena el tope de la pila en el registro fp
RET 1; +1


println@System:
LOADFP
LOADSP
STOREFP ; Almacena el tope de la pila en el registro fp
PRNLN
STOREFP ; Almacena el tope de la pila en el registro fp
RET 0; +0


printCln$char@System:
LOADFP
LOADSP
STOREFP ; Almacena el tope de la pila en el registro fp
LOAD 3 ; cargo el primer parametro
CPRINT
PRNLN
STOREFP ; Almacena el tope de la pila en el registro fp
RET 1; +1


printSln$string@System:
LOADFP
LOADSP
STOREFP ; Almacena el tope de la pila en el registro fp
LOAD 3 ; cargo el primer parametro
SPRINT
PRNLN
STOREFP ; Almacena el tope de la pila en el registro fp
RET 1; +1


read@System:
LOADFP
LOADSP
STOREFP ; Almacena el tope de la pila en el registro fp
READ ; lectura de un digito entero
PUSH 48 ; apilo el 48 para restarselo al ASCII del digito paa obtener el numero
SUB
STORE 3 ; pongo el tope de la pila en la locacion reservada
STOREFP ; Almacena el tope de la pila en el registro fp
RET 0; +0


printB$boolean@System:
LOADFP
LOADSP
STOREFP ; Almacena el tope de la pila en el registro fp
LOAD 3 ; cargo el primer parametro
BPRINT
STOREFP ; Almacena el tope de la pila en el registro fp
RET 1; +1


printIln$int@System:
LOADFP
LOADSP
STOREFP ; Almacena el tope de la pila en el registro fp
LOAD 3 ; cargo el primer parametro
IPRINT
PRNLN
STOREFP ; Almacena el tope de la pila en el registro fp
RET 1; +1


printI$int@System:
LOADFP
LOADSP
STOREFP ; Almacena el tope de la pila en el registro fp
LOAD 3 ; cargo el primer parametro
IPRINT
STOREFP ; Almacena el tope de la pila en el registro fp
RET 1; +1


printBln$boolean@System:
LOADFP
LOADSP
STOREFP ; Almacena el tope de la pila en el registro fp
LOAD 3 ; cargo el primer parametro
BPRINT
PRNLN
STOREFP ; Almacena el tope de la pila en el registro fp
RET 1; +1





