import Card from '../card/Card'
import './Board.css'

function Board({cards}) {
    const handleClick = (clickedCard) => {
       console.log(clickedCard)
    }

    return (<section className={'board'}>
        <div className="board__cards-container">
            {cards.map((card) => (<Card key={card.id} card={card} onClick={handleClick}/>))}
        </div>
    </section>)
}

export default Board