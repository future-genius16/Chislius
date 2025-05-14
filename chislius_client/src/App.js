import {Fragment, useEffect, useState} from "react"
import MenuScreen from "./components/screen/MenuScreen"
import RoomScreen from "./components/screen/RoomScreen"
import EndScreen from "./components/screen/EndScreen"
import GameScreen from "./components/screen/GameScreen"

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
            }], potions: [1, 2, 3],
            cards: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        }
        const message_4 = {
            state: States.END, players: [{id: 1, name: "Player1", score: 100, winner: true}, {
                id: 2, name: "Player2", score: 50, winner: false
            }, {
                id: 3, name: "Player3", score: 25, winner: false
            }],
        }

        const message = message_2

        setUserId(1)
        setRoomId(123456)

        if (message.state) {
            setState(message.state)

        }
        setData(message)
        setTimeout(() => {
            setData({
                ...message, cards: [0,0,0,0,0,5,6,0,0,0,0,0,0,0,0,0]
            })
        }, 1000)
        setTimeout(() => {
            setData({
                ...message, cards: [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
            })
        }, 2000)
        setTimeout(() => {
            setData({
                ...message, cards: [0,7,0,8,0,0,0,0,0,0,0,0,0,0,0,0]
            })
        }, 3000)
        setTimeout(() => {
            setData({
                ...message, cards: [0,null,0,null,0,0,0,0,0,0,0,0,0,0,0,0]
            })
        }, 4000)

    }, [])

    const renderScreen = () => {
        switch (state) {
            case States.MENU:
                return <MenuScreen userId={userId} data={data}/>

            case States.ROOM:
                return <RoomScreen userId={userId} roomId={roomId} data={data}/>

            case States.GAME:
                return <GameScreen userId={userId} roomId={roomId} data={data}/>

            case States.MOVE:
                return <GameScreen userId={userId} roomId={roomId} data={data}/>

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