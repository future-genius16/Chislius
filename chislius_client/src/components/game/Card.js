import React from 'react'
import {Container} from 'react-bootstrap'
import {Code, Cone} from 'react-bootstrap-icons'

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

    return (<Container className={`p-0 card"${!card.img == null ? ' card_invisible' : ''}`} onClick={handleClick}>
        <Container className={`p-0 card__container${!card.isOpen ? ' card_open' : ''}`}>
            <Container className="p-0 card__front">
                {getCardFrontImage()}
            </Container>
            <Container className="p-0 card__back">
                {getCardBackImage()}
            </Container>
        </Container>
    </Container>)
}

export default Card