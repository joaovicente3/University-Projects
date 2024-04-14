#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

#define TRUE 1
#define FALSE 0
#define NAO_EXISTE -1

#define BUFFER 65535

/* Estruturas de Dados */

typedef struct {
    double latitude, longitude;
    int n_carreiras;
    int *ids_carreiras;
    char *nome;
} Paragem;

typedef struct{
    Paragem origem, destino;
    int n_paragens;
    double custo, duracao;
    int *ids_paragens;
    char *nome;
} Carreira;

typedef struct{
    Carreira carreira;
    Paragem origem, destino;
    double custo, duracao;
} Ligacao;


Carreira* carreiras = NULL;     /* vetor com carreiras */

Paragem* paragens = NULL;       /* vetor com paragens */

Ligacao* ligacoes = NULL;       /* vetor com ligacoes */