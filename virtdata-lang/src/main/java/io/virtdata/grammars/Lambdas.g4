grammar Lambdas;
// https://www.youtube.com/watch?v=eW4WFgRtFeY

metagenRecipe : metagenFlow (specend metagenFlow?)* EOF ;

metagenFlow : (COMPOSE)? expression (';' expression?)* ;

expression : (lvalue ASSIGN)? metagenCall ;

metagenCall :
 ( inputType OUTPUTTYPE )?
 ( funcName '(' (arg (',' arg )* )? ')' )
 ( OUTPUTTYPE outputType )?
 ;

lvalue : ID;
inputType : ID;
funcName: ID;
outputType : ID;

arg : ( value );
ref : ('$' ID );
value : ( floatValue | doubleValue | integerValue | longValue | stringValue);
stringValue : SSTRING_LITERAL | DSTRING_LITERAL ;
longValue: LONG;
doubleValue: DOUBLE;
integerValue: INTEGER;
floatValue: FLOAT;

INTEGER : INT ;
LONG : INT ('l'|'L') ;
FLOAT
    :    '-'? INT '.' INT EXP?   // 1.35, 1.35E-9, 0.3, -4.5
    |   '-'? INT EXP            // 1e10 -3e4
    |   '-'? INT    // -3, 45
    ;
DOUBLE    :   ('-'? INT '.' INT EXP? | '-'? INT EXP | '-'? INT ) ('d'|'D') ;

fragment INT :   '0' | [1-9] [0-9]* ; // no leading zeros
fragment EXP :   [Ee] [+\-]? INT ;

specend: ( ';;' NEWLINE+ ) | ';;' | NEWLINE+ ;

NEWLINE   : '\r' '\n' | '\n' | '\r';

COMPOSE: 'compose' ;
OUTPUTTYPE: '->' ;
INPUTTYPE: '>-' ;
ASSIGN: '=';
SSTRING_LITERAL : '\'' (~('\'' | '\\' | '\r' | '\n') | '\\' ('\'' | '\\'))* '\'';
DSTRING_LITERAL : '"' (~('"' | '\\' | '\r' | '\n') | '\\' ('"' | '\\'))* '"';

// IDs can start with letters, contain numbers, dashes, and
// underscores, but not end with a dash
ID: IDPART ('.' IDPART)* ;
IDPART:  ( ( [a-zA-Z] [0-9a-zA-Z_]* )
 | ( [a-zA-Z] [0-9a-zA-Z_]* '-' [0-9a-zA-Z_]) )
 ;

WS : [\t\u000C ]+ -> channel(HIDDEN);
// NL : [\r\nu000C]
