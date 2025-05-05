import React from 'react'
import './Card.css'

function Card({card, onClick}) {

    const handleClick = () => {
        onClick(card)
    }

    const getCardImage = () => {
        return card.img
    }

    return (<article className={`card"${!card.img == null ? ' card_invisible' : ''}`} onClick={handleClick}>
        <div className={`card__container${card.isOpen ? ' card_open' : ''}`}>
            <div className="card__front"/>
            <div className="card__back">{getCardImage()}</div>
        </div>
    </article>)
}

export default Card