package AnalilzadorLexico;

public enum TipoDeToken {

    pr_class,
    pr_interface,
    pr_extends,
    pr_implements,
    pr_public,
    pr_static,
    pr_void,
    pr_boolean,
    pr_char,
    pr_int,
    pr_if,
    pr_else,
    pr_while,
    pr_return,
    pr_var,
    pr_this,
    pr_new,
    pr_null,
    pr_true,
    pr_false,
    pr_float,

    id_clase,
    id_met_var,

    op_mayor,
    op_menor,
    op_negacion,
    op_asignacion,
    op_mayor_igual,
    op_menor_igual,
    op_distinto,
    op_mas,
    op_menos,
    op_multiplicacion,
    op_division,
    op_and,
    op_or,
    op_modulo,
    op_igual,
    op_mas_igual,
    op_menos_igual,

    EOF,

    stringLiteral,
    charLiteral,

    simb_parentesis_que_abre,
    simb_parentesis_que_cierra,
    simb_llave_que_abre,
    simb_llave_que_cierra,
    simb_punto_y_coma,
    simb_coma,
    simb_punto,
    simb_comillas,

    simb_comilla_simple,

    num_int,
    car_char,
    Object,
    exp_vacia;
}