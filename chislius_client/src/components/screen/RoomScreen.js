import {Container} from 'react-bootstrap'
import React from 'react'
import RoomNavbar from '../navbar/RoomNavbar'
import Player from '../game/Player'

function RoomScreen({player, data}) {
    return (<>
        <RoomNavbar player={player} data={data}/>
        <Container className={'mt-3'}>
            <h3>Игроки <small className="text-body-secondary">({data.players.length}/{data.capacity}):</small></h3>
            <ul>
                {data.players.map(otherPlayer => (<li><Player player={otherPlayer}/>
                    {otherPlayer.id === player.id && ' (Вы)'}
                </li>))}
            </ul>
        </Container>
    </>)
}

export default RoomScreen