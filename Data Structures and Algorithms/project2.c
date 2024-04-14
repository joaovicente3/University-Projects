/*
 * File:  project2.c
 * Author:  joao_vicente
 * Description: A program that handles a bus company's stops and lines.
*/

/* iaed-23 - ist1106807 - project2 */

#include "header.h"

/* Funções de Leitura */

/* le a proxima palavra que houver no input */
int leProxPalavra(char **str){
    char c;
    int i = 0, buffer_aux = BUFFER - 1;
    c = getchar();
    while(c == ' ' || c == '\t'){
        c = getchar();
        buffer_aux--;
    }
    *str = malloc((i+1) * sizeof(char));
    while(c != ' ' && c != '\t' && c!= '\n' && c != EOF && i < buffer_aux){
        *str = realloc(*str,(i+2) * sizeof(char));
        (*str)[i] = c;
        i++;
        c = getchar();
    }
    (*str)[i] = '\0';
    return(c != '\n');
}

/* le do input o nome de uma paragem */
int leNomeParagem(char **str){
    char c;
    int i = 0, buffer_aux = BUFFER - 1;
    c = getchar();
    while(c == ' ' || c == '\t'){
        c = getchar();
        buffer_aux--;
    }
    *str = malloc((i+1) * sizeof(char));
    if(c == '\"'){
        c = getchar();
        while(c != '\"' && i < buffer_aux){
            *str = realloc(*str,(i+2) * sizeof(char));
            (*str)[i] = c;
            i++;
            c = getchar();
        }
    }  
    else{
        while(c != ' ' && c != '\t' && c!= '\n' && c != EOF && i < buffer_aux){
            *str = realloc(*str,(i+2) * sizeof(char));
            (*str)[i] = c;
            i++;
            c = getchar();
        }
    }
    (*str)[i] = '\0';
    return(c != '\n');
}

/* le do input coordenadas de uma paragem */
void leCoordenadas(double *latitude, double *longitude){
    if (scanf(" %lf %lf", latitude, longitude)){
    }
}

/* le do input o custo e a duração de uma ligação */
void leCustoEDuracao(double *custo, double *duracao){
    if (scanf(" %lf %lf", custo, duracao)){
    }
}

/* Funções Auxiliares */

/* ve se a palavra é a palavra inverso ou uma das suas abreviações */
int isInverso(char str[]){
    if(str == NULL || str[0] == '\0' || str[0] == '\n'){
        return FALSE;
    }
    else if (strcmp(str, "inv") == 0 ||
            strcmp(str, "inve") == 0 ||
            strcmp(str, "inver") == 0 ||
            strcmp(str, "invers") == 0 ||
            strcmp(str, "inverso") == 0) {
            return TRUE;
    }
    else {
        printf("incorrect sort option.\n");
        return FALSE;
    }
}

/* ve se a carreira para numa paragem */
int isIn(Paragem paragem, int n , int id){
    int i;
    for(i = 0; i < n; i++){
        if (paragem.ids_carreiras[i] == id){
            return i;
        }
    }
    return NAO_EXISTE;
}

/* obtem o num de paragens */
int getNumParagens() {
    int count = 0;
    if (paragens == NULL) {
        return 0;
    }
    while (paragens[count].nome == NULL || strcmp(paragens[count].nome, "sentinel") != 0) {
        count++;
    }
    return count;
}

/* obtem o num de carreiras */
int getNumCarreiras() {
    int count = 0;
    if (carreiras == NULL) {
        return 0;
    }
    while (carreiras[count].nome == NULL || strcmp(carreiras[count].nome, "sentinel") != 0) {
        count++;
    }
    return count;
}

/* obtem o num de ligacoes */
int getNumLigacoes() {
    int count = 0;
    if (ligacoes == NULL) {
        return 0;
    }
    while (ligacoes[count].carreira.nome == NULL || strcmp(ligacoes[count].carreira.nome, "sentinel") != 0) {
        count++;
    }
    return count;
}

/* verifica se existe e procura a posição da carreira no vetor carreiras */
int encontraCarreira(char nome[]){
    int i, n_carreiras = getNumCarreiras();
    for(i=0; i < n_carreiras; i++){
        if (carreiras[i].nome != NULL && strcmp(nome,carreiras[i].nome) == 0)
        {
            return i;
        }   
    }
    return NAO_EXISTE;
}

/* verifica se existe e procura a posição da carreira no vetor ligações */
int temLigacao(char nome[]){
    int i, n_ligacoes = getNumLigacoes();
    for(i=0; i < n_ligacoes; i++){
        if (ligacoes[i].carreira.nome != NULL && strcmp(nome,ligacoes[i].carreira.nome) == 0)
        {
            return i;
        }   
    }
    return NAO_EXISTE;
}

/* verifica se existe e procura a posição da paragem no vetor paragens */
int encontraParagem(char nome[]){
    int i, n_paragens = getNumParagens();
    for(i=0; i < n_paragens; i++){
        if (nome != NULL){
            if (paragens[i].nome != NULL && strcmp(nome,paragens[i].nome) == 0){
            return i;
            }   
        }
    }
    return NAO_EXISTE;
}    


/* Libertar Memória */

/* liberta a memória para uma paragem */
void free_paragem(Paragem *paragem) {
    if (paragem != NULL) {
        if (paragem->ids_carreiras != NULL) {
            free(paragem->ids_carreiras);
        }
        if (paragem->nome != NULL) {
            free(paragem->nome);
        }
    }
}

/* liberta a memória para uma carreira */
void free_carreira(Carreira *carreira) {
    if (carreira != NULL) {
        if (carreira->ids_paragens != NULL) {
            free(carreira->ids_paragens);
        }
        if (carreira->nome != NULL) {
            free(carreira->nome);
        }        
    }
}

/* liberta a memória para uma ligação */
void free_ligacao(Ligacao *ligacao) {
    if (ligacao != NULL) {
        free(ligacao);
    }
}

/* liberta toda a memória usada */
void libertaMemoria(){
    int i, n_paragens = getNumParagens(), 
        n_carreiras = getNumCarreiras();

    if (paragens != NULL) {
        for (i = 0; i < n_paragens+1; i++) {
            free_paragem(&paragens[i]);
        }
        free(paragens);
        paragens = NULL;
    }
    if (carreiras != NULL) {
        for (i = 0; i < n_carreiras+1; i++) {
            free_carreira(&carreiras[i]);
        }
        free(carreiras);
        carreiras = NULL;
    }
    if(ligacoes != NULL){    
        free(ligacoes);
        ligacoes = NULL;
    }
}

/* Funções C */

/* adiciona a primeira carreira */
void addPrimeiraCarreira(Carreira c){
    carreiras = realloc(carreiras, 2 * sizeof(Carreira));
    carreiras[0].custo = 0;
    carreiras[0].duracao = 0;
    carreiras[0].ids_paragens = NULL;
    carreiras[0].n_paragens = 0;

    carreiras[0].origem.nome = NULL;
    carreiras[0].destino.nome = NULL;

    carreiras[0].nome = malloc((strlen(c.nome) + 1) * sizeof(char));
    strcpy(carreiras[0].nome, c.nome);

    carreiras[1].nome = malloc(sizeof("sentinel"));
    strcpy(carreiras[1].nome, "sentinel");
    carreiras[1].ids_paragens = NULL;
}

/* adiciona uma carreira */
void addCarreira(Carreira c, int n_carreiras) {
    carreiras = realloc(carreiras, (n_carreiras+2) * sizeof(Carreira));

    carreiras[n_carreiras].custo = 0;
    carreiras[n_carreiras].duracao = 0;
    carreiras[n_carreiras].ids_paragens = NULL;
    carreiras[n_carreiras].n_paragens = 0;

    carreiras[n_carreiras].nome = realloc(carreiras[n_carreiras].nome,(strlen(c.nome) + 1) * sizeof(char));
    strcpy(carreiras[n_carreiras].nome, c.nome);

    carreiras[n_carreiras+1].nome = malloc(sizeof("sentinel"));
    strcpy(carreiras[n_carreiras+1].nome, "sentinel");
    carreiras[n_carreiras+1].ids_paragens = NULL;
}

/* realiza as funções do comando c */
void handle_c(){
    Carreira c;
    int i ,id_c, n_carreiras = getNumCarreiras();
    char *inv = NULL, *nome_aux = NULL;
    c.nome = NULL;
    if(leProxPalavra(&nome_aux)){      /* ve se há algo escrito depois do nome */
        leProxPalavra(&inv);
    }
    if (nome_aux[0] != '\0'){
        c.nome = realloc(c.nome,(strlen(nome_aux) + 1) * sizeof(char));
        strcpy(c.nome, nome_aux);
    }
    if(c.nome == NULL){                      /* sem argumentos */
        for(i = 0; i < n_carreiras; i++){
            if(carreiras[i].nome != NULL){
                if(carreiras[i].n_paragens > 1){
                    printf("%s %s %s %d %.2f %.2f\n", 
                            carreiras[i].nome, carreiras[i].origem.nome,
                            carreiras[i].destino.nome, carreiras[i].n_paragens,
                            carreiras[i].custo, carreiras[i].duracao);
                }    
                else{
                    printf("%s 0 0.00 0.00\n", carreiras[i].nome);
                } 
            }   
        }
    }
    else if (encontraCarreira(c.nome) == -1){
        /* carreira que nao estava no sistema */
        if(n_carreiras == 0){
            addPrimeiraCarreira(c);
        }
        else{    
            addCarreira(c, n_carreiras);
        }
    }
    else{
        id_c = encontraCarreira(c.nome);
        if (isInverso(inv) == FALSE && carreiras[id_c].n_paragens > 0){    
            /* carreira no sistema, sem inverso */
            for(i=0; i < carreiras[id_c].n_paragens-1; i++){
                printf("%s, ", paragens[carreiras[id_c].ids_paragens[i]].nome);
            }
            printf("%s\n", paragens[carreiras[id_c].ids_paragens[
                            carreiras[id_c].n_paragens-1]].nome);
        }
        else if (isInverso(inv) == TRUE && carreiras[id_c].n_paragens > 0) {
            /* carreira no sistema, com inverso */
            for(i = carreiras[id_c].n_paragens-1; i > 0; i--){
                printf("%s, ", paragens[carreiras[id_c].ids_paragens[i]].nome);
            }
            printf("%s\n", paragens[carreiras[id_c].ids_paragens[0]].nome);
        }
        free(inv);
    }
    free(nome_aux);
    free(c.nome);
}


/* Funções P */

/* adiciona a primeira paragem */
void addPrimeiraParagem(Paragem p) {
    paragens = realloc(paragens, 2 * sizeof(Paragem));

    paragens[0].latitude = p.latitude;
    paragens[0].longitude = p.longitude;
    paragens[0].n_carreiras = p.n_carreiras;
    paragens[0].ids_carreiras = NULL;

    paragens[0].nome = malloc((strlen(p.nome) + 1) * sizeof(char));
    strcpy(paragens[0].nome, p.nome);

    paragens[1].nome = malloc(sizeof("sentinel"));
    strcpy(paragens[1].nome, "sentinel");
    paragens[1].ids_carreiras = NULL;
}

/* adiciona uma paragem */
void addParagem(Paragem p, int n_paragens) {
    paragens = realloc(paragens, (n_paragens+2) * sizeof(Paragem));

    paragens[n_paragens].latitude = p.latitude;
    paragens[n_paragens].longitude = p.longitude;
    paragens[n_paragens].n_carreiras = p.n_carreiras;
    paragens[n_paragens].ids_carreiras = NULL;

    paragens[n_paragens].nome = malloc((strlen(p.nome) + 1) * sizeof(char));
    strcpy(paragens[n_paragens].nome, p.nome);

    paragens[n_paragens+1].nome = malloc(sizeof("sentinel"));
    strcpy(paragens[n_paragens+1].nome, "sentinel");
    paragens[n_paragens+1].ids_carreiras = NULL;
}

/* realiza as funções do comando p */
void handle_p(){
    Paragem p;
    int i, id, temCoordenadas = FALSE, n_paragens = getNumParagens();
    char *nome_aux = NULL; 
    p.nome = NULL;
    if(leNomeParagem(&nome_aux) && nome_aux != NULL){
        temCoordenadas = TRUE;        
    }
    p.nome = malloc((strlen(nome_aux) + 1) * sizeof(char));
    strcpy(p.nome, nome_aux);
    if(p.nome[0] != '\0'){                      /* se há argumentos */
        if(temCoordenadas == TRUE){             /* se foram dadas coordenadas */
            leCoordenadas(&p.latitude, &p.longitude);
            if(encontraParagem(p.nome) != -1){  /* se existe no sistema */
                printf("%s: stop already exists.\n", p.nome);
            }
            else{
                p.n_carreiras = 0;
                if(n_paragens == 0){
                    addPrimeiraParagem(p);
                }
                else{
                    addParagem(p, n_paragens);
                }   
            }
        }
        else{
            if(encontraParagem(p.nome) == -1){     
                printf("%s: no such stop.\n", p.nome);
            }
            else{
                id = encontraParagem(p.nome);
                printf("%16.12f %16.12f\n", 
                        paragens[id].latitude, paragens[id].longitude);
            }
        }
    }
    else{
        for(i=0; i < n_paragens; i++){
            if(paragens[i].nome != NULL){
                printf("%s: %16.12f %16.12f %d\n",
                    paragens[i].nome,paragens[i].latitude,
                    paragens[i].longitude, paragens[i].n_carreiras);
            }
        }
    }
    free(nome_aux);
    free(p.nome);
}


/* Funções L */

/* adiciona a paragem dada no inicio da cerreira */
void addCarreiraAParagem(int id_c, int id_p){
    if(isIn(paragens[id_p], paragens[id_p].n_carreiras, id_c) == NAO_EXISTE){
        paragens[id_p].n_carreiras++;
        paragens[id_p].ids_carreiras = realloc(paragens[id_p].ids_carreiras, 
                                        paragens[id_p].n_carreiras * sizeof(int));
        paragens[id_p].ids_carreiras[paragens[id_p].n_carreiras-1] = id_c;
    }
}

/* adiciona a uma carreira a sua primeira ligação e paragens */
void adicionaPrimeiraLigacao(int id_c, int id_p_o, int id_p_d,
                             double custo, double duracao){
    carreiras[id_c].origem = paragens[id_p_o];
    carreiras[id_c].destino = paragens[id_p_d];
    carreiras[id_c].n_paragens = 2;
    carreiras[id_c].ids_paragens = malloc(2 * sizeof(int));
    carreiras[id_c].ids_paragens[0] = id_p_o;
    carreiras[id_c].ids_paragens[1] = id_p_d;
    carreiras[id_c].custo = custo;
    carreiras[id_c].duracao = duracao;
    addCarreiraAParagem(id_c, id_p_o);
    addCarreiraAParagem(id_c, id_p_d);
}

/* adicona ao inicio lista de ids das paragens da carreira */
void adiconaParagemNoInicio(int id_c, int id_p_o, double custo, double duracao){
    int i = carreiras[id_c].n_paragens;
    carreiras[id_c].ids_paragens = realloc(carreiras[id_c].ids_paragens,
                                        (carreiras[id_c].n_paragens+1) * sizeof(int));
    while (i > 0) {
        carreiras[id_c].ids_paragens[i] = carreiras[id_c].ids_paragens[i - 1];
        i--;
    }
    carreiras[id_c].ids_paragens[0] = id_p_o;
    carreiras[id_c].n_paragens++;
    carreiras[id_c].origem = paragens[id_p_o];
    carreiras[id_c].custo += custo;
    carreiras[id_c].duracao += duracao;
    addCarreiraAParagem(id_c, id_p_o);
}

/* adiciona a paragem dada no fim da carreira */
void adiconaParagemNoFim(int id_c, int id_p_d, double custo, double duracao){
    carreiras[id_c].ids_paragens = realloc(carreiras[id_c].ids_paragens,
                                        (carreiras[id_c].n_paragens+1) * sizeof(int));
    carreiras[id_c].ids_paragens[carreiras[id_c].n_paragens++] = id_p_d;
    carreiras[id_c].destino = paragens[id_p_d];
    carreiras[id_c].custo += custo;
    carreiras[id_c].duracao += duracao;

    addCarreiraAParagem(id_c, id_p_d);
}

/* adiciona a primeira ligação */
void addPrimeiraLigacao(Ligacao l){
    ligacoes = realloc(ligacoes, 2 * sizeof(Ligacao));
    ligacoes[0] = l;

    ligacoes[1].carreira.nome = malloc(sizeof("sentinel"));
    strcpy(ligacoes[1].carreira.nome, "sentinel");
    ligacoes[1].carreira.ids_paragens = NULL;
}

/* adicona ligação ao vetor de ligações */
void addLigacao(Ligacao l, int n_ligacoes){
    ligacoes = realloc(ligacoes, (n_ligacoes+2) * sizeof(Ligacao));
    ligacoes[n_ligacoes] = l;

    ligacoes[n_ligacoes+1].carreira.nome = malloc(sizeof("sentinel"));
    strcpy(ligacoes[n_ligacoes+1].carreira.nome, "sentinel");
    ligacoes[n_ligacoes+1].carreira.ids_paragens = NULL;
}

/* adiciona uma ligação, dependendo do input dado */
void adicionaLigacao(int id_c, int id_p_o, int id_p_d, Ligacao l){
    int n_ligacoes = getNumLigacoes();
    if(l.custo >= 0 && l.duracao >= 0){
        if (strcmp(carreiras[id_c].destino.nome, l.origem.nome) == 0 &&
                strcmp(carreiras[id_c].origem.nome, l.destino.nome) != 0){ 
            /* destino da carreira igual a origem da ligacao e origem
             diferente de destino, correspondentemente */
            /* linear, adiciona no fim */
            adiconaParagemNoFim(id_c, id_p_d, l.custo, l.duracao);
        }
        else if (strcmp(carreiras[id_c].destino.nome, l.origem.nome) != 0 &&
                strcmp(carreiras[id_c].origem.nome, l.destino.nome) == 0){ 
            /* origem da carreira igual a destino da ligacao e destino
             diferente de origem, correspondentemente */
            /* linear, adiciona no inicio */
            adiconaParagemNoInicio(id_c, id_p_o, l.custo, l.duracao);
        }
        else if (strcmp(carreiras[id_c].destino.nome, l.origem.nome) == 0 &&
                strcmp(carreiras[id_c].origem.nome, l.destino.nome) == 0){ 
            /* origem da carreira igual a destino da ligacao e destino igual
            de origem, correspondentemente */
            /* circular, adiciona no fim */
            adiconaParagemNoFim(id_c, id_p_d, l.custo, l.duracao);
        }
        addLigacao(l, n_ligacoes);
    }
    else{
        printf("negative cost or duration.\n");
    }
}

/* realiza as funções do comando l */
void handle_l(){
    Ligacao l;
    int id_c, id_p_o, id_p_d, n_ligacoes = getNumLigacoes();
    char *nome_aux = NULL;
    leProxPalavra(&nome_aux);
    l.carreira.nome = malloc((strlen(nome_aux) + 1) * sizeof(char));
    strcpy(l.carreira.nome, nome_aux);
    leNomeParagem(&l.origem.nome);
    leNomeParagem(&l.destino.nome);
    leCustoEDuracao(&l.custo,&l.duracao);
    id_c = encontraCarreira(l.carreira.nome);
    id_p_o = encontraParagem(l.origem.nome);
    id_p_d = encontraParagem(l.destino.nome);
    if(id_c == -1){         /* ve se a carreira existe */
        printf("%s: no such line.\n", l.carreira.nome);
        return;
    }
    if(id_p_o == -1){       /* ve se a paragem de origem existe */
        printf("%s: no such stop.\n",l.origem.nome);
        return;
    }
    if(id_p_d == -1 && id_p_o != -1){ 
        /* ve se a paragem de destino existe e se a de origem existe */
        printf("%s: no such stop.\n",l.destino.nome);
        return;
    }
    if(id_c != -1 && id_p_o != -1 && id_p_d != -1 &&
        carreiras[id_c].n_paragens == 0){   
        /* se não tiver paragens */
        if(l.custo >= 0 && l.duracao >= 0){
            adicionaPrimeiraLigacao(id_c, id_p_o, id_p_d, l.custo, l.duracao);
            addLigacao(l, n_ligacoes);
        }
        else{
            printf("negative cost or duration.\n");
        }
    }
    else if (strcmp(carreiras[id_c].destino.nome, l.origem.nome) != 0 &&
            strcmp(carreiras[id_c].origem.nome, l.destino.nome) != 0){
            /* ve se a paragem de destino da ligacao e diferente 
            a da origem ou vice versa*/
            printf("link cannot be associated with bus line.\n");
            return;
            if(l.custo < 0 || l.duracao < 0){ 
            /* se o custo ou a duracao sao negativos */
                printf("negative cost or duration.\n");
            }
        }
    else {
        adicionaLigacao(id_c, id_p_o, id_p_d, l);
    }
    free(nome_aux);
}


/* Funções I */

/* faz bubblessort das carreiras da paragem e dá print ao sorted array */
void bubbleSortandPrint(char** carr, int n) {
    int i, j;
    char* aux;
    for(i = 0; i < n-1; i++) {
        for(j = 0; j < n-i-1; j++) {
            if(strcmp(carr[j], carr[j+1]) > 0) {
                aux = carr[j];
                carr[j] = carr[j+1];
                carr[j+1] = aux;
            }
        }
    }
    for (i = 0; i < n-1; i++) {
        printf("%s ", carr[i]);
    }
    printf("%s\n", carr[n-1]);
}

/* realiza as funções do comando i */
void handle_i() {
    int i, j, n_paragens = getNumParagens();
    for (i = 0; i < n_paragens; i++) {
        if (paragens[i].nome != NULL && paragens[i].n_carreiras > 1) {
            char** toSort = malloc(sizeof(char*) * paragens[i].n_carreiras);
            for (j = 0; j < paragens[i].n_carreiras; j++) {
                toSort[j] = carreiras[paragens[i].ids_carreiras[j]].nome;
            }
            printf("%s %d: ", paragens[i].nome, paragens[i].n_carreiras);
            bubbleSortandPrint(toSort, paragens[i].n_carreiras);
            free(toSort);
        }
    }
}


/* Funções R */

/* remove carreira da lista de carreiras da paragem */
void removeCarreiraDeParagem(int id_p, int id_in){
    int i;
    paragens[id_p].n_carreiras--;

    for(i = id_in; i < paragens[id_p].n_carreiras; i++){
        paragens[id_p].ids_carreiras[i] = paragens[id_p].ids_carreiras[i+1];
    }

    paragens[id_p].ids_carreiras = realloc(paragens[id_p].ids_carreiras,
                                            paragens[id_p].n_carreiras * sizeof(int));
}

/* reset à informação da carreira */
void resetCarreira(Carreira *c) {
    c->n_paragens = 0;
    c->custo = 0.0;
    c->duracao = 0.0;   

    if (c->ids_paragens != NULL) {
        free(c->ids_paragens);
        c->ids_paragens = NULL;
    }
    if (c->nome != NULL) {
        free(c->nome);
        c->nome = NULL;
    }
}

/* reset à informação da carreira exceto o nome */
void resetCarreiraMenosNome(Carreira *c) {
    c->n_paragens = 0;
    c->custo = 0.0;
    c->duracao = 0.0;   

    if (c->ids_paragens != NULL) {
        free(c->ids_paragens);
        c->ids_paragens = NULL;
    }
}

/* realiza as funções do comando r */
void handle_r(){
    Carreira c;
    char *nome_aux = NULL;
    int id_c, id_p, id_l, n_paragens = getNumParagens(), id_in, n_ligacoes = getNumLigacoes();
    leProxPalavra(&nome_aux);
    c.nome = malloc((strlen(nome_aux) + 1) * sizeof(char));
    strcpy(c.nome, nome_aux);
    id_c = encontraCarreira(c.nome);

    if(id_c == -1){
        printf("%s: no such line.\n", c.nome);
    }
    else{
        for(id_p = 0; id_p < n_paragens; id_p++){
            id_in = isIn(paragens[id_p], paragens[id_p].n_carreiras, id_c);
            if (paragens[id_p].nome != NULL && id_in != NAO_EXISTE){
                removeCarreiraDeParagem(id_p, id_in);
            }
        }

        for(id_l = 0; id_l < n_ligacoes; id_l++){
            if(ligacoes[id_l].carreira.nome != NULL && strcmp(c.nome,ligacoes[id_l].carreira.nome) == 0){
                ligacoes[id_l].carreira.nome = NULL;
            }
        }
        resetCarreira(&carreiras[id_c]);
    }
    free(nome_aux);
    free(c.nome);
}


/* Funções E */

/* encontra o id da ligacao com a carreira e paragens dadas */
int encontraLigacao(int id_c, int id_p_o, int id_p_d){
    int i, n_ligacoes = getNumLigacoes();

    for(i = 0; i < n_ligacoes; i++){
        if(ligacoes[i].carreira.nome != NULL && 
            strcmp(ligacoes[i].carreira.nome, carreiras[id_c].nome) == 0 &&
            ligacoes[i].origem.nome != NULL &&
            strcmp(ligacoes[i].origem.nome, paragens[id_p_o].nome) == 0 &&
            ligacoes[i].destino.nome != NULL &&
            strcmp(ligacoes[i].destino.nome, paragens[id_p_d].nome) == 0){
            return i;
        }
    }

    return NAO_EXISTE;
}

/* remove paragem da lista de paragens da carreira */
void removeParagemDeCarreira(int id_c, int id_p){
    int i;
    for(i = 0; i < carreiras[id_c].n_paragens; i++){
        if(carreiras[id_c].ids_paragens[i] == id_p){
            break;
        }
    }
    for(; i < carreiras[id_c].n_paragens - 1; i++){
        carreiras[id_c].ids_paragens[i] = carreiras[id_c].ids_paragens[i+1];
    }
    carreiras[id_c].n_paragens--;
    if(carreiras[id_c].n_paragens == 1){
        resetCarreiraMenosNome(&carreiras[id_c]);
    }
    
    carreiras[id_c].ids_paragens = realloc(carreiras[id_c].ids_paragens, 
                                            carreiras[id_c].n_paragens * sizeof(int));
}

/* reset da informação da paragem */
void resetParagem(Paragem *p) {
    p->n_carreiras = 0;
    p->latitude = 0;
    p->longitude = 0;   

    if (p->ids_carreiras != NULL) {
        free(p->ids_carreiras);
        p->ids_carreiras = NULL;
    }
    if (p->nome != NULL) {
        free(p->nome);
        p->nome = NULL;
    }
}

/* reset da informação da ligação */
void resetLigacao(Ligacao *l){
    l->custo = 0.00;
    l->duracao = 0.00;
    l->carreira.nome = NULL;
    l->origem.nome = NULL;
    l->destino.nome = NULL;
}

/* remove uma paragem que não estava nas pontas da carreira */
void removeParagemMeio(int id_c, int id_p, int i){
    Ligacao new_l;
    int id_origem_l1 = carreiras[id_c].ids_paragens[i-1], id_destino_l2 = carreiras[id_c].ids_paragens[i+1],
        n_ligacoes = getNumLigacoes(), id_l1 = encontraLigacao(id_c, id_origem_l1, id_p), 
        id_l2 = encontraLigacao(id_c, id_p, id_destino_l2);

    new_l.carreira = carreiras[id_c];
    new_l.origem = paragens[id_origem_l1];
    new_l.destino = paragens[id_destino_l2];    
    new_l.custo = ligacoes[id_l1].custo + ligacoes[id_l2].custo;
    new_l.duracao = ligacoes[id_l1].duracao + ligacoes[id_l2].duracao;
    addLigacao(new_l, n_ligacoes);

    removeParagemDeCarreira(id_c, id_p);
    
    if(carreiras[id_c].n_paragens == 2){
        carreiras[id_c].origem = paragens[id_origem_l1];
        carreiras[id_c].destino = paragens[id_destino_l2];
    }
    
    resetLigacao(&ligacoes[id_l1]);
    resetLigacao(&ligacoes[id_l2]);
}

/* realiza as funções do comando e */
void handle_e(){
    Paragem p;
    int id_c, i, id_p, id_p_aux, id_l, n_carreiras = getNumCarreiras();
    char *nome_aux = NULL; 
    p.nome = NULL;
    leNomeParagem(&nome_aux);      
    p.nome = malloc((strlen(nome_aux) + 1) * sizeof(char));
    strcpy(p.nome, nome_aux);
    id_p = encontraParagem(p.nome);

    if(id_p == -1){
        printf("%s: no such stop.\n", p.nome);
    }
    else{
        for(id_c = 0; id_c < n_carreiras; id_c++){
            if(carreiras[id_c].nome != NULL){
                for(i=0; i < carreiras[id_c].n_paragens; i++){
                    if(carreiras[id_c].ids_paragens[0] == id_p){
                        id_p_aux = carreiras[id_c].ids_paragens[1];
                        id_l = encontraLigacao(id_c, id_p, id_p_aux);
                        carreiras[id_c].custo -= ligacoes[id_l].custo;
                        carreiras[id_c].duracao -= ligacoes[id_l].duracao;
                        removeParagemDeCarreira(id_c, id_p);
                        carreiras[id_c].origem = paragens[id_p_aux];
                        i = 0;
                    }
                    
                    if(carreiras[id_c].ids_paragens[carreiras[id_c].n_paragens-1] == id_p){
                        id_p_aux = carreiras[id_c].ids_paragens[carreiras[id_c].n_paragens-2];
                        id_l = encontraLigacao(id_c,id_p_aux,id_p);
                        carreiras[id_c].custo -= ligacoes[id_l].custo;
                        carreiras[id_c].duracao -= ligacoes[id_l].duracao;
                        removeParagemDeCarreira(id_c,id_p);
                        carreiras[id_c].destino = paragens[id_p_aux];
                        i = 0;
                    }
                }

                for(i=1; i < carreiras[id_c].n_paragens - 1; i++){
                    if(carreiras[id_c].ids_paragens[i] == id_p){
                    removeParagemMeio(id_c, id_p, i);
                    i--;   
                    }
                }
            }
        }
        resetParagem(&paragens[id_p]);
    }
    free(nome_aux);
    free(p.nome);
}


/* Main */

int main(){
    char c;
    while((c = getchar()) != EOF){
        switch (c) {
        case 'q':
            libertaMemoria();
            return 0;
        case 'c':
            handle_c();
            break;
        case 'p':
            handle_p();
            break;
        case 'l':
            handle_l();
            break;
        case 'i':
            handle_i();
            break;
        case 'r':
            handle_r();
            break;
        case 'e':
            handle_e();
            break;
        case 'a':
            libertaMemoria();     
        }
    }
    return 0;
}