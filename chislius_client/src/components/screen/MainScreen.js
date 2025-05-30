import React, {useEffect, useState} from 'react'
import MenuScreen from './MenuScreen'
import RoomScreen from './RoomScreen'
import EndScreen from './EndScreen'
import {useAuth} from '../../context/TokenContext'
import LoadingSpinner from '../utils/LoadingSpinner'
import GameScreen from './GameScreen'
import {States} from '../utils/States'
import {over} from 'stompjs'
import SockJS from 'sockjs-client'
import api from '../../client/ApiClient'

function MainScreen() {
    const {token, logout} = useAuth()

    const [state, setState] = useState(null)
    const [player, setPlayer] = useState(null)
    const [data, setData] = useState(null)
    const [connected, setConnected] = useState(false)

    const stompClient = over(new SockJS('http://localhost:8080/ws'))

    useEffect(() => {
        if (token === null) return

        api.update(token).then(() => {
            stompClient.connect({}, onConnected, onError)
        }).catch(() => {
            if (connected) {
                console.log('Disconnect')
                stompClient.disconnect()
                setConnected(false)
            }
            console.log('Logout')
            logout()
        })

        return () => {
            if (connected) {
                console.log('Disconnect')
                stompClient.disconnect()
                setConnected(false)
            }
        }

    }, [token])

    const onConnected = () => {
        console.log('Connected')
        setConnected(true)
        stompClient.subscribe('/user/' + token + '/update', onMessage)
        api.update(token).catch(() => {
            console.log('Logout')
            logout()
            if (connected) {
                console.log('Disconnect')
                stompClient.disconnect()
                setConnected(false)
            }
        })
    }

    const onMessage = (response) => {
        var data = JSON.parse(response.body)
        setData(data.data)
        setState(data.state)
        setPlayer(data.player)
    }

    const onError = (err) => {
        console.error('WebSocket error:', err)
        if (connected) {
            console.log('Disconnect')
            stompClient.disconnect()
            setConnected(false)
        }
        setState(null)
        setConnected(false)
        logout()
    }

    const renderScreen = () => {
        switch (state) {
            case States.MENU:
                return <MenuScreen player={player}/>

            case States.ROOM:
                return <RoomScreen player={player} data={data}/>

            case States.GAME:
            case States.MOVE:
                // return <GameScreen player={player} state={state} data={data}/>

            case States.END:
                return <EndScreen player={player} data={data}/>

            default:
                return <LoadingSpinner/>
        }
    }

    return (<div className="main_screen">
        {renderScreen()}
    </div>)
}

export default MainScreen