import RoomNavbar from '../navbar/RoomNavbar'
import {Container} from 'react-bootstrap'
import Player from '../game/Player'
import React from 'react'

function EndScreen({player, data}) {
    return (<>
        <RoomNavbar player={player} data={data}/>
        <Container className={'mt-3'}>
        <h1>Игра завершена!</h1>
        <h2>Результаты:</h2>
        <ul>
            {data.players.map(otherPlayer => (<li><Player player={otherPlayer}/>
                {otherPlayer.id === player.id && ' (Вы)'}<b> Счет: {otherPlayer.score}</b></li>))}
        </ul>
        </Container>
    </>)
}

export default EndScreen