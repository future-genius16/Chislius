import {Fragment, useEffect, useState} from "react"

const States = {
    MENU: 0, ROOM: 1, GAME: 2, MOVE: 3, END: 4
}

function App() {
    const [state, setState] = useState(States.MENU)
    const [userId, setUserId] = useState(null)
    const [roomId, setRoomId] = useState(null)
    const [data, setData] = useState(null)

    useEffect(() => {
        const message_0 = {
            state: States.MENU, score: 1000
        }
        const message_1 = {
            state: States.ROOM,
            players: [{id: 1, name: "Player1", ready: true}, {id: 2, name: "Player2", ready: false}, {
                id: 3, name: "Player3", ready: true
            }]
        }
        const message_2 = {
            state: States.GAME, players: [{id: 1, name: "Player1", score: 100}, {id: 2, name: "Player2", score: 50}, {
                id: 12, name: "Player3", score: 25
            }], potions: [1, 2, 3], cards: [1, 2, 3, null, 5, 6, null, 8],
        }
        const message_3 = {
            state: States.MOVE, players: [{id: 1, name: "Player1", score: 100}, {id: 2, name: "Player2", score: 50}, {
                id: 12, name: "Player3", score: 25
            }], potions: [1, 2, 3], cards: [1, 2, 3, null, 5, 6, null, 8],
        }
        const message_4 = {
            state: States.END, players: [{id: 1, name: "Player1", score: 100, winner: true}, {
                id: 2, name: "Player2", score: 50, winner: false
            }, {
                id: 3, name: "Player3", score: 25, winner: false
            }],
        }

        const message = message_0

        setUserId(1)
        setRoomId(123456)

        if (message.state) {
            setState(message.state)

        }
        setData(message)
    }, [])

    const renderScreen = () => {
        switch (state) {
            case States.MENU:
                return <MenuScreen userId={userId} data={data}/>

            case States.ROOM:
                return <RoomScreen userId={userId} roomId={roomId} data={data}/>

            case States.GAME:
                return <GameScreenTemp userId={userId} roomId={roomId} data={data}/>

            case States.MOVE:
                return <MoveScreen userId={userId} roomId={roomId} data={data}/>

            case States.END:
                return <EndScreen userId={userId} roomId={roomId} data={data}/>

            default:
                return <div>Loading...</div>
        }
    }

    return (<>
        <h1>Числиус</h1>
        {userId && <div>User ID: {userId}</div>}
        {roomId && <div>Room ID: {roomId}</div>}
        <div>Текущее состояние: {state}</div>
        <div>
            {renderScreen()}
        </div>
    </>)
}

export default App

const MenuScreen = ({userId, data}) => {

    return (<>
        <h1>Главное меню</h1>
        <p>Ваш ID: {userId}</p>
        {data && <p>Ваш счет: {data.score}</p>}
        <button>Найти игру</button>
    </>)
}

const RoomScreen = ({userId, roomId, data}) => {
    return (<>
        <h1>Комната #{roomId}</h1>
        <h2>Игроки:</h2>
        <ul>
            {data?.players?.map(player => (<li key={player.id}>
                {player.name} {player.ready ? " Готов " : " Не готов "}
                {player.id === userId && " (Вы)"}
            </li>))}
        </ul>
        <button>Готов</button>
    </>)
}

const GameScreenTemp = ({userId, roomId, data}) => {
    return (<>
        <h1>Игра идет!</h1>
        <div>
            <h2>Игроки:</h2>
            <ul>
                {data?.players?.map(player => (<li key={player.id}>
                    {player.name}: {player.score} очков
                    {player.id === userId && " (Вы)"}
                </li>))}
            </ul>
        </div>

        <div>
            <h2>Зелья:</h2>
            <div>
                {data?.potions?.map((potion, index) => (<div key={index}>Зелье #{potion}</div>))}
            </div>

            <h2>Карты:</h2>
            <div>
                {data?.cards?.map((card, index) => (<div key={index}>
                    {card ? `Карта ${card}` : "Пусто"}
                </div>))}
            </div>
        </div>
    </>)
}

const MoveScreen = ({userId, roomId, data}) => {
    return (<div>
        <h1>Ваш ход!</h1>
        <div>
            <h2>Игроки:</h2>
            <ul>
                {data?.players?.map(player => (<li key={player.id}>
                    {player.name}: {player.score} очков
                    {player.id === userId && " (Вы)"}
                </li>))}
            </ul>
        </div>

        <div>
            <h2>Зелья:</h2>
            <div>
                {data?.potions?.map((potion, index) => (<div key={index}>Зелье #{potion}</div>))}
            </div>

            <h2>Карты:</h2>
            <div>
                {data?.cards?.map((card, index) => (<div key={index}>
                    {card ? `Карта ${card}` : "Пусто"}
                </div>))}
            </div>
        </div>

        <div>
            <button>Использовать зелье</button>
            <button>Взять карту</button>
            <button>Закончить ход</button>
        </div>
    </div>)
}

const EndScreen = ({userId, roomId, data}) => {
    return (<>
        <h1>Игра завершена!</h1>
        <h2>Результаты:</h2>
        <ul>
            {data?.players?.map(player => (<li key={player.id}>
                {player.name}: {player.score} очков
                {player.winner && " 🏆"}
                {player.id === userId && " (Вы)"}
            </li>))}
        </ul>
        <button>В главное меню</button>
    </>)
}
