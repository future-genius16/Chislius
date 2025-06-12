import React from 'react'
import {Image} from 'react-bootstrap'

function Player({player, hideRating = false}) {
    return (<>
        <Image src={'/images/profiles/' + player.avatar + '.png'} roundedCircle width={32} height={32}/>
        <b> {player.name}</b>{!hideRating && <> | Рейтинг {player.rating}</>}
    </>)
}

export default Player