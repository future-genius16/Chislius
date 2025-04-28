import React, {useState, useEffect} from 'react'
import Card from '../card/Card'
import './Board.css'

function Board({cards, setCards, setCanSubmit}) {
    const [openCount, setOpenCount] = useState(0)

    useEffect(() => {
        const idxArray = []
        for (let i = 1; i <= 16; i++) {
            idxArray.push(i)
        }
        const cardsArray = idxArray.map(id => ({id: id, isOpen: false}));
        setCards(cardsArray)
    }, [setCards])


    useEffect(() => {
        const openCards = cards.filter(item => item.isOpen)
        setCanSubmit(openCards.length >= 2 && openCards.length <= 3)
        setOpenCount(openCards.length)
    }, [cards, setCanSubmit])


    const handleClick = (clickedCard) => {
        if (!clickedCard.isOpen && openCount < 3) {
            setCards(cards.map((item) => (item.id === clickedCard.id ? {...item, isOpen: true} : item)))
        }
    }

    return (<section className={'board'}>
        <div className="board__cards-container">
            {cards.map((card) => (<Card key={card.id} card={card} onClick={handleClick}/>))}
        </div>
    </section>)
}

export default Board