import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AmountInWords {
  convertNumberToWords(amount): string {
    const words = [];
    words[0] = '';
    words[1] = 'One';
    words[2] = 'Two';
    words[3] = 'Three';
    words[4] = 'Four';
    words[5] = 'Five';
    words[6] = 'Six';
    words[7] = 'Seven';
    words[8] = 'Eight';
    words[9] = 'Nine';
    words[10] = 'Ten';
    words[11] = 'Eleven';
    words[12] = 'Twelve';
    words[13] = 'Thirteen';
    words[14] = 'Fourteen';
    words[15] = 'Fifteen';
    words[16] = 'Sixteen';
    words[17] = 'Seventeen';
    words[18] = 'Eighteen';
    words[19] = 'Nineteen';
    words[20] = 'Twenty';
    words[30] = 'Thirty';
    words[40] = 'Forty';
    words[50] = 'Fifty';
    words[60] = 'Sixty';
    words[70] = 'Seventy';
    words[80] = 'Eighty';
    words[90] = 'Ninety';

    amount = amount.toString();
    const atemp = amount.split('.');
    const number = atemp[0].split(',').join('');
    const paisa = atemp[1] ? (atemp[1].length === 1 ? atemp[1] + '0' : atemp[1]) : null;
    let paisaStr = '';
    const numberLength = number.length;

    let wordsString = '';
    if (numberLength <= 9) {
      const numberArray: number[] = [0, 0, 0, 0, 0, 0, 0, 0, 0];
      const receivedNarray: number[] = [];
      for (let i = 0; i < numberLength; i++) {
        receivedNarray[i] = +number.substr(i, 1);
      }
      for (let i = 9 - numberLength, j = 0; i < 9; i++, j++) {
        numberArray[i] = +receivedNarray[j];
      }
      for (let i = 0, j = 1; i < 9; i++, j++) {
        if (i === 0 || i === 2 || i === 4 || i === 7) {
          if (numberArray[i] === 1) {
            numberArray[j] = 10 + +numberArray[j];
            numberArray[i] = 0;
          }
        }
      }
      let value = 0;
      for (let i = 0; i < 9; i++) {
        if (i === 0 || i === 2 || i === 4 || i === 7) {
          value = +numberArray[i] * 10;
        } else {
          value = +numberArray[i];
        }
        if (value !== 0) {
          wordsString += words[value] + ' ';
        }
        if ((i === 1 && value !== 0) || (i === 0 && value !== 0 && numberArray[i + 1] === 0)) {
          wordsString += 'Crores ';
        }
        if ((i === 3 && value !== 0) || (i === 2 && value !== 0 && numberArray[i + 1] === 0)) {
          wordsString += 'Lakhs ';
        }
        if ((i === 5 && value !== 0) || (i === 4 && value !== 0 && numberArray[i + 1] === 0)) {
          wordsString += 'Thousand ';
        }
        if (i === 6 && value !== 0 && (numberArray[i + 1] !== 0 || numberArray[i + 2] !== 0)) {
          wordsString += 'Hundred and ';
        } else if (i === 6 && value !== 0) {
          wordsString += 'Hundred ';
        }
      }

      // for paisa
      if (paisa) {
        if (paisa.length === 1) paisaStr = words[+paisa];
        else {
          const paisa1 = paisa.split('')[0];
          const paisa10 = paisa.split('')[1];

          if (+paisa1 === 1) paisaStr = words[+paisa];
          else {
            paisaStr = words[+paisa1 * 10];
            paisaStr += ' ' + words[paisa10];
          }
        }
      } else paisaStr = null;

      wordsString = wordsString.split('  ').join(' ');
      wordsString += wordsString ? ' Taka' : '';
      wordsString += paisaStr ? ' ' + paisaStr + ' Paisa.' : '.';
    }
    return wordsString;
  }
}
