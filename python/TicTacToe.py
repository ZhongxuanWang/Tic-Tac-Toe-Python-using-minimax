
import platform
from os import system

HUMAN = -1
COMP = +1
board = [
    [0, 0, 0],
    [0, 0, 0],
    [0, 0, 0],
]
maxscore = 0
minscore = 0


def evaluate(state):
    if(wins(state, COMP)):
        return COMP
    elif(wins(state, HUMAN)):
        return HUMAN
    else:
        return 0


def wins(state, player):
    win_state = [
        [state[0][0], state[0][1], state[0][2]],
        [state[1][0], state[1][1], state[1][2]],
        [state[2][0], state[2][1], state[2][2]],
        [state[0][0], state[1][0], state[2][0]],
        [state[0][1], state[1][1], state[2][1]],
        [state[0][2], state[1][2], state[2][2]],
        [state[0][0], state[1][1], state[2][2]],
        [state[2][0], state[1][1], state[0][2]],
    ]
    if [player, player, player] in win_state:
        return True
    else:
        return False


def empty_cells(state):
    cells = []
    for x, row in enumerate(state):
        for y, cell in enumerate(row):
            if cell == 0:
                cells.append([x, y])
    return cells


def validmove(*posi):
    if [posi[0], posi[1]] in empty_cells(board):
        return True
    else:
        return False


def set_move(player, state, *posi):
    x = posi[0]
    y = posi[1]
    state[x][y] = player


def clean():
    os_name = platform.system().lower()
    if 'windows' in os_name:
        system('cls')
    else:
        system('clear')


def printboard():
    print()
    print("  1 2 3")
    for row, col in enumerate(board):
        print(row + 1, end="")
        for con in col:
            if(con == 0):
                a = " -"
            if(con == 1):
                a = " O"
            if(con == -1):
                a = " X"
            print(a, end="")
        print()
    print()


def comturn():
    emcells = empty_cells(board)
    depth = len(emcells)
    # This step is to maximize usage. Select the best 2 chances.
    if depth == 8:
        print(emcells)
        if [1, 1] in emcells:
            set_move(COMP, board, *[1, 1])
        else:
            set_move(COMP, board, *[0, 0])
    else:
        # Invoke minimax to culculate.
        # State is a virtual board that meant to give machine.
        set_move(COMP, board, *minimax(depth, board, COMP))
    return

# State: The current map


def minimax(depth, state, role):

    if ro le == COMP:
        best = [-1, -1, -1]
    else:
        best = [-1, -1, +1]

    if depth == 0 or wins(state, HUMAN) or wins(state, COMP):
        score = evaluate(state)
        return [-1, -1, score]

    for cell in empty_cells(state):
        x, y = cell[0], cell[1]
        state[x][y] = role
        score = minimax(depth - 1, state, -role)
        state[x][y] = 0
        score[0], score[1] = x, y

        if role == COMP:
            if score[2] > best[2]:
                best = score  # max value
        else:
            if score[2] < best[2]:
                best = score  # min value

    return best


def main():
    printboard()
    while True:
        x, y = eval(input("Y,X: "))
        posi = [x-1, y-1]
        if validmove(*posi):
            set_move(HUMAN, board, *posi)
            if wins(board, HUMAN):
                print("You win!")
                break
            clean()
            comturn()
            printboard()
            if wins(board, COMP):
                print("You Lost!")
                break
        else:
            clean()
            printboard()
            print('Bad input.')


if(__name__ == "__main__"):
    main()
