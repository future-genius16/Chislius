import React from 'react';
import './Card.css';

function Card({card, onClick}) {

    const handleClick = () => {
        onClick(card);
    };

    return (<article className="card" onClick={handleClick}>
        <div className={`card__container${card.isOpen ? ' card_open' : ''}`}>
            <div className="card__front"/>
            <div className="card__back">{card.id}</div>
        </div>
    </article>);
}

export default Card;