import React from 'react'
import './Card.css'

function Card({card, onClick}) {

    const handleClick = () => {
        onClick(card)
    }

    const getCardImage = () => {
        if (card.img === null) {
            return <img
                src={`/images/cards/disabled.svg`}
                alt={`Card ${card.img}`}
                className="card__image"
            />
        }
        return <img
            src={`/images/cards/${card.img}.svg`}
            alt={`Card ${card.img}`}
            className="card__image"
        />
    }

    return (<article className={`card"${!card.img == null ? ' card_invisible' : ''}`} onClick={handleClick}>
        <div className={`card__container${card.isOpen ? ' card_open' : ''}`}>
            <div className="card__front">
                <img
                    src={`/images/cards/0.svg`}
                    alt={`Card ${card.img}`}
                    className="card__image"
                />
            </div>
            <div className="card__back">{getCardImage()}</div>
        </div>
    </article>)
}

export default Card