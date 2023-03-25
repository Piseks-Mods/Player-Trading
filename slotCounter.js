let slotIn = 0;
let slotOut = 3;
let slotInLocal = 0;
let slotOutLocal = 1;

for (let i = 1; i <= 6; i++){
slotIn = slotIn + 4;
slotOut = slotOut +4;
slotInLocal = slotInLocal + 2;
slotOutLocal = slotOutLocal + 2;

console.log(`--- Repetetion: ${i} ---`);
console.log(`slotInLocal: ${slotInLocal}`);
console.log(`slotOutLocal: ${slotOutLocal}`);
console.log(`slotIn: ${slotIn}`);
console.log(`slotOut: ${slotOut}`);
console.log("---------------------")
}
