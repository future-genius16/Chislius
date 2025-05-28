import React, {useEffect, useState} from 'react'
import MenuScreen from './MenuScreen'
import RoomScreen from './RoomScreen'
import EndScreen from './EndScreen'
import api from '../../client/ApiClient'
import {useAuth} from '../../context/TokenContext'
import LoadingSpinner from '../LoadingSpinner'
import GameScreen from './GameScreen'
import {States} from '../room/States'

function MainScreen() {
    const {logout} = useAuth()
    const [state, setState] = useState(null)
    const [player, setPlayer] = useState(null)
    const [data, setData] = useState(null)

    useEffect(() => {
        let intervalId = setInterval(async () => {
            try {
                const response = await api.update()
                console.log(response)
                setState(response.state)
                setPlayer(response.player)
                setData(response.data)
            } catch (err) {
                setState(null)
                setData(null)
                setPlayer(null)
                logout()
                clearInterval(intervalId)
            }
        }, 1000)
        return () => {
            clearInterval(intervalId)
        }
    }, [])

    const renderScreen = () => {
        switch (state) {
            case States.MENU:
                return <MenuScreen player={player}/>

            case States.ROOM:
                return <RoomScreen player={player} data={data}/>

            case States.GAME:
            case States.MOVE:
                return <GameScreen player={player} state={state} data={data}/>

            case States.END:
                return <EndScreen player={player} data={data}/>

            default:
                return <LoadingSpinner/>
        }
    }

    return (<>
        {renderScreen()}
    </>)
}

export default MainScreen