import React from 'react'
import {Card, Image} from 'react-bootstrap'

function Player({player}) {
    return (<>
        <Image src={'/images/profiles/' + (player.rating * player.rating) % 12 + '.png'} roundedCircle width={32} height={32}/> <b> {player.name}</b>
    </>)
}

export default Player