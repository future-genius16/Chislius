import React from 'react'
import './Card.css'

function Card({card, onClick}) {

    const handleClick = () => {
        onClick(card)
    }

    const getCardFrontImage = () => {
        return <img
            src={`/images/cards/${card.img}.svg`}
            alt={`Card ${card.img}`}
            className="card__image"
        />
    }

    const getCardBackImage = () => {
        if (!card.isDisabled) {
            return <img
                src={`/images/cards/back.svg`}
                alt={`Card ${card.img}`}
                className="card__image"
            />
        } else {
            return <img
                src={`/images/cards/disabled.svg`}
                alt={`Card ${card.img}`}
                className="card__image"
            />
        }
    }

    return (<article className={`card"${!card.img == null ? ' card_invisible' : ''}`} onClick={handleClick}>
        <div className={`card__container${!card.isOpen ? ' card_open' : ''}`}>
            <div className="card__front">
                {getCardFrontImage()}
            </div>
            <div className="card__back">
                {getCardBackImage()}
            </div>
        </div>
    </article>)
}

export default Card