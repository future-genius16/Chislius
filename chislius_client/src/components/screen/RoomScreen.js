import {Container} from 'react-bootstrap'
import React from 'react'
import RoomNavbar from '../navbar/RoomNavbar'

function RoomScreen({player, data}) {
    return (<>
        <RoomNavbar player={player} data={data}/>
        <Container>
            <h1>Комната {data.code}</h1>
            <h2>Игроки:</h2>
            <ul>
                {data.players.map(otherPlayer => (<li key={otherPlayer.id}>
                    {otherPlayer.name}
                    {otherPlayer.id === player.id && ' (Вы)'}
                </li>))}
            </ul>
        </Container>
    </>)
}

export default RoomScreen