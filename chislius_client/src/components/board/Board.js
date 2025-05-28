import Card from '../card/Card'
import './Board.css'

function Board({onClick, cards}) {
    return (<section className={'board'}>
        <div className="board__cards-container">
            {cards.map((card) => (<Card key={card.id} card={card} onClick={onClick}/>))}
        </div>
    </section>)
}

export default Board