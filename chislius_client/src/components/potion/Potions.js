function Potions({potions}) {
    const handleClick = (clickedPotion) => {
        console.log(clickedPotion)
    }

    return (<section className="potions">
        <div className="potions__container">
            {potions.map((potion) => (<Potion key={potion.id} potion={potion} onClick={handleClick}/>))}
        </div>
    </section>)
}

function Potion({potion, onClick}) {
    const getPotionImage = () => {
        if (potion.img === null) {
            return (<img
                src={`/images/potions/disabled.svg`}
                alt={`Potion Disabled`}
                className="potion__image"
            />)
        }
        return (<img
            src={`/images/potions/${potion.color}_${potion.value}_${potion.quantity}.svg`}
            alt={`Potion ${potion.color} ${potion.value} ${potion.quantity} `}
            className="potion__image"
        />)
    }

    return (
        <article className={`potion${potion.img === null ? ' potion_invisible' : ''}`} onClick={() => onClick(potion)}>
            <div className={`potion__container${potion.isOpen ? ' potion_open' : ''}`}>
                {getPotionImage()}
            </div>
        </article>)
}

export default Potions