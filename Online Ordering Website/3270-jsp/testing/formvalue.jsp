<form action="" id="cakeform" onsubmit="return false;">
   <fieldset>
    <legend>Make your cake!</legend>
    <label >Size Of the Cake</label>
    <input type="radio"  name="selectedcake" value="Round6"
    onclick="calculateTotal()" />
    Round cake 6" -  serves 8 people ($20)
    <input type="radio"  name="selectedcake" value="Round8"
    onclick="calculateTotal()" />
    Round cake 8" - serves 12 people ($25)
    <input type="radio"  name="selectedcake" value="Round10"
    onclick="calculateTotal()" />
    Round cake 10" - serves 16 people($35)
    <input type="radio"  name="selectedcake" value="Round12"
    onclick="calculateTotal()" />
    Round cake 12" - serves 30 people($75)
 
    <label >Filling</label>
 
    <select id="filling" name='filling'
    onchange="calculateTotal()">
    <option value="None">Select Filling</option>
    <option value="Lemon">Lemon($5)</option>
    <option value="Custard">Custard($5)</option>
    <option value="Fudge">Fudge($7)</option>
    <option value="Mocha">Mocha($8)</option>
    <option value="Raspberry">Raspberry($10)</option>
    <option value="Pineapple">Pineapple($5)</option>
    <option value="Dobash">Dobash($9)</option>
    <option value="Mint">Mint($5)</option>
    <option value="Cherry">Cherry($5)</option>
    <option value="Apricot">Apricot($8)</option>
    <option value="Buttercream">Buttercream($7)</option>
    <option value="Chocolate Mousse">Chocolate Mousse($12)</option>
   </select>
    <br/>
    <p>
    <label for='includecandles' class="inlinelabel">
    Include Candles($5)</label>
    <input type="checkbox" id="includecandles" name='includecandles'
    onclick="calculateTotal()" />
   </p>
 
    <p>
    <label class="inlinelabel" for='includeinscription'>
    Include Inscription($20)</label>
    <input type="checkbox" id="includeinscription"
    name="includeinscription" onclick="calculateTotal()" />
 
    <input type="text"  id="theinscription"
    name="theinscription" value="Enter Inscription"  />
    </p>
    <div id="totalPrice"></div>
 
    </fieldset>
</form>