import RoomNavbar from '../navbar/RoomNavbar'
import {Container} from 'react-bootstrap'
import Player from '../game/Player'
import React from 'react'
import {Trophy} from 'react-bootstrap-icons'

function EndScreen({player, data}) {
    const players = data.players.toSorted((a, b) => b.score - a.score)
    const max = Math.max(...players.map(player => player.score))
    return (<>
        <RoomNavbar player={player} data={data}/>
        <Container className={'mt-3'}>
            <h1>Игра завершена!</h1>
            <h5>Больше не осталось возможных ходов</h5>
            <h2 className={'mt-3'}>Результаты:</h2>
            <ul>
                {players.map(otherPlayer => (<li className={'mb-1'}><Player player={otherPlayer} hideRating={true}/>
                    {otherPlayer.id === player.id && ' (Вы)'}<b> |
                        Счет: {otherPlayer.score}</b> {otherPlayer.score === max && <Trophy size={20}/>}</li>))}
            </ul>
        </Container>
    </>)
}

export default EndScreen