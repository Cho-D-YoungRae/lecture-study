#include<stdio.h>
#include<string.h>
#include<stdlib.h>


//노드 구조체
typedef struct node {
    struct node* prev;
    char elem;
    struct node* next;

}Node;

//노드 리스트의 헤드노드와 테일노드, 리스트의 길이들을 갖는 리스트
typedef struct _list {
    Node* head;
    int size;
    Node* tail;
}List;

/**
 * 1.
 * 전반적으로 모든 함수에서 입력과 함수 동작 수행이 둘다 포함되어 있는데
 * 분리되는 것이 좋아보임
 * -> 한가지 함수에서 2가지 이상의 일을 처리하는 것
 *
 * 2. 함수 및 변수 들의 이름 짓기에 조금 더 신경쓰기
 */

/**
 * 리스트 포인터의 변수가 pList 가 더 좋아보임
 */
//리스트 초기화 함수
void init(List* lptr) {
    Node* ohead, * otail;
    ohead = (Node*)malloc(sizeof(Node)); //동적할당
    otail = (Node*)malloc(sizeof(Node)); // 동적할당

    lptr->head = ohead;
    ohead->prev = NULL;
    ohead->next = otail;


    lptr->tail = otail;
    otail->next = NULL;
    otail->prev = ohead;

    /**
     * tail, head 노드는 값이 들어있는 노드가 아니기 때문에 없는 것으로 보고
     * 초기화시 리스트의 크기는 0으로 초기화 하는 것이 더 좋아보임
     */
    lptr->size = 2;
}

//리스트에 노드한개를 추가하는 함수
void add(List* lptr) {

    char newletter;
    int order;
    scanf("%d %c", &order, &newletter);//노드의 위치,값 입력받기
    getchar();

    /**
     * 실습문제 명세를 보면 add는 비어있는 리스트의 경우 rank 1부터 삽입이 가능함. 즉 리스트 크기보다 1 더 큰 값까지 허용.
     * -> rank 1 은 현재 리스트에 없지만 (리스트에 아무 값도 들어있지 않으므로) 빈 자리로 보고 삽입 가능
     * 하지만 del, get 은 반드시 원소가 있는 rank 에 대해서만 가능하기 때문에
     * add 에서는 -1 일때 정상적으로 작동하고 del, get  등은 -2 에서 정상적으로 동작
     */
    if ((lptr->size) - 1 < order) { //위치에 문제가 있다면
        printf("invalid position\n"); // 에러 출력
        return;
    }

    Node* temp;
    temp = lptr->head;

    Node* newnode; // 노드 동적할당 해주기
    newnode = (Node*)malloc(sizeof(Node));
    newnode->elem = newletter;


    for (int i = 0; i < order; i++) { // 새 노드의 자리 find
        temp = temp->next;
    }

    newnode->next = temp;
    newnode->prev = temp->prev;
    temp->prev = newnode;
    temp = newnode->prev;
    temp->next = newnode;

    (lptr->size)++;

}

//list의 순위 r에 위치한 원소를 삭제하는 함수
void del(List* lptr) {
    int order;

    scanf("%d", &order); //위치 입력
    getchar();

    if ((lptr->size) - 2 < order) {
        printf("invalid position\n");
        return;
    }

    Node* temp, * ptemp, * ntemp;

    temp = lptr->head;
    //위치 찾기
    for (int i = 0; i < order; i++) {
        temp = temp->next;
    }

    ptemp = temp->prev;
    ntemp = temp->next;
    ptemp->next = ntemp;
    ntemp->prev = ptemp;
    free(temp); //동적할당 해제

    (lptr->size)--;
}

//list의 순위 r에 위치한 원소를 반환하는 함수
void get(List* lptr) {
    Node* temp;
    temp = lptr->head;

    int rank;
    scanf("%d", &rank);
    getchar();

    if ((lptr->size) - 2 < rank) {
        printf("invalid position\n");
        return;
    }

    //위치 찾기
    for (int i = 0; i < rank; i++) {
        temp = temp->next;
    }
    //해당위치의 노드의 값만 출력
    printf("%c\n", temp->elem);
}

// list의 모든 원소를 저장 순위대로 공백X 출력하는 함수
void print(List* lptr) {
    Node* temp;
    temp = lptr->head;
    //노드를 한 번 옮긴 뒤 출력
    for (int i = 0; i < (lptr->size) - 2; i++) {
        temp = temp->next;
        printf("%c", temp->elem);
    }
    printf("\n");//한줄 띄기
}

//main 함수
int main() {
    int n;
    char command;

    List* L;
    L = (List*)malloc(sizeof(List));
    init(L);
    //리스트를 만든 뒤 동적할당,초기화

    scanf("%d", &n);//연산개수 입력
    getchar();

    /**
     * if 문 안에 continue 는 필요 없어보임. -> 어처피 저 중 1가지만 실행되기 때문
     */
    for (int i = 0; i < n; i++) {
        scanf("%c", &command);
        getchar();

        if (command == 'A') { //A일때
            add(L); //추가 함수 호출
            continue;
        }
        else if (command == 'D') { //D일때
            del(L); // 삭제함수 호출
            continue;
        }
        else if (command == 'G') { //G일때
            get(L); // r순위에 위치한 원소 반환함수 호출
            continue;
        }
        else if (command == 'P') { //P일때
            print(L); //전체 출력함수 호출
            continue;
        }
        else {
            printf("");
        }
    }

    return 0;
}
